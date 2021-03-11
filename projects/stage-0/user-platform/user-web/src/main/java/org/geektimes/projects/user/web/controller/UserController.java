package org.geektimes.projects.user.web.controller;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.repository.DatabaseUserRepository;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.projects.user.service.impl.UserServiceImpl;
import org.geektimes.projects.user.sql.DBConnectionManager;
import org.geektimes.web.mvc.controller.PageController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author coy
 * @since 2021/3/1
 **/
@Path("/user")
public class UserController implements PageController {
//    private String databaseUrl = "jdbc:derby:db/user-platform;create=true";
//    private Connection connection = DriverManager.getConnection(databaseUrl);
//    private DBConnectionManager connectionManager = new DBConnectionManager(connection);
    private UserService userService = new UserServiceImpl(new DatabaseUserRepository());
    
    public UserController() throws SQLException {
    }
    
    /**
     * @param request  HTTP 请求
     * @param response HTTP 相应
     * @return 视图地址路径
     */
    @Override
    @Path("/register")
    @FormParam("")
    @POST
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        //1、拿到jsp表单参数，进行注册
        //2、跳转到注册成功页面
        User user = new User();
        user.setName(request.getParameter("name"));
        user.setPassword(request.getParameter("password"));
        user.setPhoneNumber(request.getParameter("phoneNumber"));
        user.setEmail(request.getParameter("inputEmail"));
        userService.register(user);
        return "success.jsp";
    }
}
