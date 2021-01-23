package demo.wh.admin.service.impl;

import demo.wh.admin.dao.UserDao;
import demo.wh.admin.dao.impl.UserDaoImpl;
import demo.wh.admin.domain.Address;
import demo.wh.admin.domain.User;
import demo.wh.admin.service.UserService;
import demo.wh.admin.commons.TableData;

import java.util.Date;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();

    @Override
    public TableData<User> getPageData(String name, String gender, String email, Date beginRegisterDate, Date endRegisterDate, Integer beginIndex, Integer pageSize) {
        return userDao.getPageData(name, gender, email, beginRegisterDate, endRegisterDate, beginIndex, pageSize);
    }

    @Override
    public TableData<Address> getAddressById(Integer userId) {
        return userDao.getAddressById(userId);
    }

    @Override
    public void activeStatusById(Integer userId) {
        userDao.activeStatusById(userId);
    }
}
