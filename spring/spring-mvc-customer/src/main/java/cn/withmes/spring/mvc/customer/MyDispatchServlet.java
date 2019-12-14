/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年12月15日
 */
package cn.withmes.spring.mvc.customer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ClassName: MyDispathServerLet
 * @Description:
 * @author leegoo
 * @date 2019年12月15日
 */
public class MyDispatchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void init() throws ServletException {
        System.out.println("servlet init .....");
    }
}
