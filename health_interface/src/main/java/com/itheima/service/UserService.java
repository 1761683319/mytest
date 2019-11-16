package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;

import java.util.List;

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

    User findUserByUsername(String username);


    User findByUserName(String username);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    List<Role> findRoleList();

    void add(com.itheima.pojo.User user, Integer[] roleIds);

    User findById(Integer id);

    List<Integer> findroleIds(Integer id);

    void edit(User user, Integer[] roleIds);

    void delete(Integer id);
}
