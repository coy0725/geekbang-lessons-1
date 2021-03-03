package org.geektimes.projects.user.service.impl;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.repository.UserRepository;
import org.geektimes.projects.user.service.UserService;

import java.sql.SQLException;

/**
 * @author coy
 * @since 2021/3/2
 **/
public class UserServiceImpl implements UserService {
    
    private UserRepository userRepository;
    
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * 注册用户
     *
     * @param user 用户对象
     * @return 成功返回<code>true</code>
     */
    @Override
    public boolean register(User user) throws SQLException {
        return userRepository.save(user);
    }
    
    /**
     * 注销用户
     *
     * @param user 用户对象
     * @return 成功返回<code>true</code>
     */
    @Override
    public boolean deregister(User user) {
        return false;
    }
    
    /**
     * 更新用户信息
     *
     * @param user 用户对象
     * @return
     */
    @Override
    public boolean update(User user) {
        return false;
    }
    
    @Override
    public User queryUserById(Long id) {
        return null;
    }
    
    @Override
    public User queryUserByNameAndPassword(String name, String password) {
        return null;
    }
}
