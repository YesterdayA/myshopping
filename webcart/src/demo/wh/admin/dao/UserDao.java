package demo.wh.admin.dao;

import demo.wh.admin.domain.Address;
import demo.wh.admin.domain.User;
import demo.wh.admin.commons.TableData;

import java.util.Date;

public interface UserDao {
    //获取bootstrapTable能接受的数据User
    TableData<User> getPageData(String name, String gender, String email,
                                Date beginRegisterDate, Date endRegisterDate,
                                Integer beginIndex, Integer pageSize
    );
    //获取bootstrapTable能接受的数据Address
    TableData<Address> getAddressById(Integer userId);
    //改变Status的值
    void activeStatusById(Integer userId);
}
