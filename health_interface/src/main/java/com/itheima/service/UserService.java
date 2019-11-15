package com.itheima.service;

import com.itheima.pojo.User;

/**
 * 用户接口服务
 * @author wangxin
 * @version 1.0
 */
public interface UserService {
    /**
     * 根据用户名查询用户信息（角色 权限）
     * @param username
     * @return
     */
    User findByUserName(String username);
}
