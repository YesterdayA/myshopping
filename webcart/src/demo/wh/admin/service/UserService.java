package demo.wh.admin.service;

import demo.wh.admin.domain.Address;
import demo.wh.admin.domain.User;
import demo.wh.admin.commons.TableData;

import java.util.Date;

public interface UserService {
    TableData<User> getPageData(String name, String gender, String email,
                                Date beginRegisterDate, Date endRegisterDate,
                                Integer beginIndex, Integer pageSize
    );
    TableData<Address> getAddressById(Integer userId);
    void activeStatusById(Integer userId);

}
