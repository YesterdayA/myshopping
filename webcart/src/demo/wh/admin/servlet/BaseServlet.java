package demo.wh.admin.servlet;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class BaseServlet extends HttpServlet {
    protected void sendJson(HttpServletResponse resp, Object obj) throws ServletException, IOException {
        String jsonString = JSONObject.toJSONString(obj);
        //返回json的MIME类型
        resp.setContentType("application/json;charset=utf-8");
        //告诉浏览器谁可以拿这些数据
        resp.setHeader("Access-Control-Allow-Origin", "*");
        //将数据返回给前端
        PrintWriter writer = resp.getWriter();
        writer.write(jsonString);
        writer.flush();
        writer.close();

    }
}

