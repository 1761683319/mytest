package com.itheima.dao;

import com.itheima.pojo.User;

/**
 * 用户接口
 * @author wangxin
 * @version 1.0
 */
public interface UserDao {
    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    User findByUserName(String username);
}
