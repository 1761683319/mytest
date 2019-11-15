package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;

import java.util.List;
import java.util.Map;

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
    User findUserByUsername(String username);

    User findByUserName(String username);

    Page<User> pageQuery(String queryString);

    List<Role> findRoleList();

    void add(User user);

    void addUserAndRole(Map<String, Object> map);

    User findById(Integer id);

    List<Integer> findroleIds(Integer id);

    void edit(User user);

    void deleteUserIdByRoleIds(Integer id);

    long findRoleCountByUserId(Integer id);

    void delete(Integer id);
}
