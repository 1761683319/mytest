package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Permission;

import java.util.Set;

/**
 * 权限服务接口
 * @author wangxin
 * @version 1.0
 */
public interface PermissionDao {
    /**
     * 根据角色id查询权限集合
     * @param roleId
     * @return
     */
    Set<Permission> findPermissionByRoleId(Integer roleId);

    Page<Permission> findPage(String queryString);

    void add(Permission permission);

    Permission findById(Integer id);

    void edit(Permission permission);

    void delete(Integer id);

    long findRoleCountByPermissionsId(Integer id);
}
