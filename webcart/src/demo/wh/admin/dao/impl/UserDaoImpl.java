package demo.wh.admin.dao.impl;

import demo.wh.admin.dao.UserDao;
import demo.wh.admin.domain.Address;
import demo.wh.admin.domain.User;
import demo.wh.admin.commons.TableData;
import demo.wh.utils.DBUtils;
import demo.wh.utils.DataUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class UserDaoImpl implements UserDao {
    /**
     * select u_id id ,u_name name ,u_email email,u_sex sex,u_status status,u_phone phone,u_register_date registerDate
     * from user where 1=1
     */
    @Override
    public TableData<User> getPageData(String name, String sex, String email, Date beginRegisterDate, Date endRegisterDate, Integer beginIndex, Integer pageSize) {
        Connection connection = DBUtils.getConnection();
        List<User> userList = getAll(connection, name, sex, email, beginRegisterDate, endRegisterDate, beginIndex, pageSize);
        int total = getTotal(connection, name, sex, email, beginRegisterDate, endRegisterDate);
        TableData<User> tableData = new TableData<>(total,userList);
        DBUtils.close(null, null, connection);
        return tableData;

    }

    /**
     *
     * select a_id id,a_name name,a_phone phone,a_detail detail a_state status from address
     * where 1=1 and u_id = userId
     * @param userId
     * @return
     */

    @Override
    public TableData<Address> getAddressById(Integer userId) {
        Connection connection = DBUtils.getConnection();
        List<Address> addressById = getAddressListById(connection, userId);
        Integer total = getAddressTotal(connection, userId);
        TableData<Address> tableData = new TableData<>(total,addressById);
        return tableData;
    }

    //改变status的状态
    @Override
    public void activeStatusById(Integer userId) {
        String sql = "update user set u_status=1 where u_id = ?";
        Connection connection=DBUtils.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1,userId);
            statement.executeUpdate();
            DBUtils.close(null,statement,connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //根据id来获取对应的地址
    private List<Address> getAddressListById(Connection connection, Integer userId){
       List<Address> list = null;
        String sql1 = "select a_id id,a_name name,a_phone phone,a_detail detail, a_state status from address where u_id = ?";
        //String sql2 = "select count(*) from address where u_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql1);
            statement.setObject(1,userId);
            ResultSet rs = statement.executeQuery();
            list = DataUtils.getAll(Address.class, rs);
            DBUtils.close(rs,statement,null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private Integer getAddressTotal(Connection connection,Integer userId){
        String sql = "select count(*) from address where u_id =?";
        int total = 0;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setObject(1,userId);
            ResultSet rs = statement.executeQuery();
            rs.next();
            total = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    //根据参数获取分页的数据
    private List<User> getAll(Connection connection, String name, String sex, String email, Date beginRegisterDate, Date endRegisterDate, Integer beginIndex, Integer pageSize) {
        List<User> userList = null;
        StringBuffer sb = new StringBuffer("select u_id id ,u_name name ,u_email email,u_sex sex,u_status status,u_phone phone,u_register_date registerDate from user where 1=1 ");
        if (null != name && !"".equals(name)) {
            sb.append(" and u_name like ? ");
        }
        if (!"-1".equals(sex)) {
            sb.append(" and u_sex = ? ");
        }
        if (null != email && !"".equals(email)) {
            sb.append(" and u_email like ? ");
        }
        if (null != beginRegisterDate) {
            sb.append(" and u_register_date > ? ");
        }
        if (null != endRegisterDate) {
            sb.append(" and u_register_date < ? ");
        }
        sb.append(" limit ? ,?");
        try {
            PreparedStatement statement = connection.prepareStatement(sb.toString());
            int i = 0;
            if (null != name && !"".equals(name)) {
                statement.setObject(++i, "%" + name + "%");
            }
            if (!"-1".equals(sex)) {
                statement.setObject(++i, sex);
            }
            if (null != email && !"".equals(email)) {
                statement.setObject(++i, "%" + email + "%");
            }
            if (null != beginRegisterDate) {
                statement.setObject(++i, beginRegisterDate);

            }
            if (null != endRegisterDate) {
                statement.setObject(++i, endRegisterDate);
            }
            statement.setObject(++i, beginIndex);
            statement.setObject(++i, pageSize);

            ResultSet resultSet = statement.executeQuery();
            userList = DataUtils.getAll(User.class, resultSet);
            DBUtils.close(resultSet, statement, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList;
    }

    private int getTotal(Connection connection, String name, String sex, String email, Date beginRegisterDate, Date endRegisterDate) {
        int total = 0;
        StringBuffer sb = new StringBuffer("select count(*) from user where 1=1 ");
        if (null != name && !"".equals(name)) {
            sb.append(" and u_name like ? ");
        }
        if (!"-1".equals(sex)) {
            sb.append(" and u_sex = ? ");
        }
        if (null != email && !"".equals(email)) {
            sb.append(" and u_email like ? ");
        }
        if (null != beginRegisterDate) {
            sb.append(" and u_register_date > ? ");
        }
        if (null != endRegisterDate) {
            sb.append(" and u_register_date < ? ");
        }

        try {
            PreparedStatement statement = connection.prepareStatement(sb.toString());
            int i = 0;
            if (null != name && !"".equals(name)) {
                statement.setObject(++i, "%" + name + "%");
            }
            if (!"-1".equals(sex)) {
                statement.setObject(++i, sex);
            }
            if (null != email && !"".equals(email)) {
                statement.setObject(++i, "%" + email + "%");
            }
            if (null != beginRegisterDate) {
                statement.setObject(++i, beginRegisterDate);

            }
            if (null != endRegisterDate) {
                statement.setObject(++i, endRegisterDate);
            }
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            total = resultSet.getInt(1);
            DBUtils.close(resultSet, statement, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }


}
