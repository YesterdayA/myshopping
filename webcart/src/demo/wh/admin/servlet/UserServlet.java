package demo.wh.admin.servlet;

import demo.wh.admin.domain.Address;
import demo.wh.admin.domain.User;
import demo.wh.admin.service.UserService;
import demo.wh.admin.service.impl.UserServiceImpl;
import demo.wh.admin.commons.TableData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet(value = "/user", name = "UserServlet")
public class UserServlet extends BaseServlet {
    private UserService userService = new UserServiceImpl();

    /**
     * 利用反射通过method方法的值来调用方法
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String mtd = req.getParameter("method");
        try {
            Method method = UserServlet.class.getDeclaredMethod(mtd, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    //激活状态
    private void activeStatus(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        userService.activeStatusById(Integer.parseInt(userId));
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("code", 1);
            map.put("msg", "success");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", -1);
            map.put("msg", "激活失败");
        }
        sendJson(resp, map);
    }

    //获取所有的用户信息
    private void getUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //http://localhost:8080/shop_war_exploded/user?order=asc&offset=10&limit=10&method=getUser
        String beginIndex = req.getParameter("offset");
        String pageSize = req.getParameter("limit");
        String name = req.getParameter("name");
        String sex = req.getParameter("sex");
        String email = req.getParameter("email");
        String status = req.getParameter("status");
        Date beginRegisterDate = formatDate(req.getParameter("beginRegisterDate"));
        Date endRegisterDate = formatDate(req.getParameter("endRegisterDate"));
        TableData<User> tableData = userService.getPageData(name, sex, email, beginRegisterDate,
                endRegisterDate, Integer.parseInt(beginIndex), Integer.parseInt(pageSize));
        sendJson(resp, tableData);
    }

    //获取地址
    private void getAddress(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        TableData<Address> addressById = userService.getAddressById(Integer.parseInt(userId));
        sendJson(resp, addressById);

    }

    private Date formatDate(String str) {
        if (str == null || "".equals(str.trim())) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = null;
        try {
            dateTime = simpleDateFormat.parse(str);
            return dateTime;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }
}
