package org.geektimes.projects.user.context;

import org.geektimes.projects.user.sql.DBConnectionManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import java.sql.Connection;
import java.util.NoSuchElementException;

/**
 * 组件上下文
 * @author coy
 * @since 2021/3/9
 **/
public class ComponentContext {
    
    public static String CONTEXT_NAME = ComponentContext.class.getSimpleName();
    private static ServletContext servletContext;
    
    
    private Context context;
    
    
    /**
     * 获取 ComponentContext
     * @return
     */
    public static ComponentContext getInstance(){
        return (ComponentContext)servletContext.getAttribute(CONTEXT_NAME);
    }
    
    public void init(ServletContext servletContext)  {
        try {
            //依赖查找
            context= (Context) new InitialContext().lookup("java:comp/env");
        } catch (RuntimeException | NamingException e) {
            throw new RuntimeException(e);
        }
        ComponentContext.servletContext = servletContext;
        servletContext.setAttribute(CONTEXT_NAME, this);
        
        
    }
    
    /**
     * 通过名称进行依赖查找
     * @param name
     * @param <C>
     * @return
     * @throws NamingException
     */
    public <C> C getComponent(String name)  {
        C component = null;
        try {
            component = (C) context.lookup(name);
        } catch (RuntimeException | NamingException e) {
            throw new NoSuchElementException("context 中没有该组件："+name);
        }
        return component;
    }
    
    public void destroy() throws NamingException {
        if (this.context != null) {
            context.close();
            
        }
    }
    
    
}
