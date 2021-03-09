package org.geektimes.projects.user.web.listener;

import org.geektimes.projects.user.context.ComponentContext;
import org.geektimes.projects.user.sql.DBConnectionManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * {@link  ComponentContext} 初始化器
 */
public class ComponentContextInitializerListener implements ServletContextListener {
    
    private ServletContext servletContext;
    
   

   
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ComponentContext context = new ComponentContext();
        this.servletContext = sce.getServletContext();
        context.init(servletContext);
        servletContext.setAttribute(ComponentContext.CONTEXT_NAME,context);
//        initDateBase();
    }
    
    private void initDateBase() {
        Connection connection = getConnection();
        try {
            DBConnectionManager dbConnectionManager = ComponentContext.getInstance().getComponent("bean/DBConnectionManager");
            dbConnectionManager.initDateBase(connection);

        }catch (Exception e){
            e.printStackTrace();
        }
        
        
    }
    
    private Connection getConnection() {
        Context context;
        Connection connection = null;
        try {
            context = new InitialContext();
            //依赖查找
            Context envCtx = (Context) context.lookup("java:comp/env");
    
            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/UserPlatformDB");
             connection = ds.getConnection();
            
        } catch (NamingException | SQLException e) {
            servletContext.log(e.getMessage(),e);
            
        }
        if (connection != null) {
            servletContext.log("获取 JNDI 数据库连接成功！");
        }
        return connection;
    }
    
    
    
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
