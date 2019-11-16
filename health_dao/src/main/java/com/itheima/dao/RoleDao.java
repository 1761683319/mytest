package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 角色服务接口
 * @author wangxin
 * @version 1.0
 */
public interface RoleDao {
    /**
     * 根据用户id查询角色信息
     * @param userId
     * @return
     */
    Set<Role> findRoleByUserId(Integer userId);
    Set<Role> findRoles(String username);

    Page<Role> findPage(String queryString);

    List<Permission> findPermissionList();

    void add(Role role);

    void addRoleAndPermission(Map<String, Object> map);

    Role findById(Integer id);

    List<Integer> findpermissionIds(Integer id);

    void deleteRoleIdByPermissionIds(Integer id);

    void edit(Role role);

    long findRoleIdByPermissionCount(Integer id);

    long findRoleIdByMenuCount(Integer id);

    long findUserByRoleId(Integer id);

    void delete(Integer id);

    void addRoleAndMenu(Map<String, Object> map);

    List<Integer> findMenuIds(Integer id);

    void deleteRoleIdByMenuIds(Integer id);
}
