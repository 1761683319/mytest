package com.itheima.dao;

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
}
