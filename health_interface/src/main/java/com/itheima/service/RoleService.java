package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;

import java.util.List;

public interface RoleService {

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    List<Permission> findPermissionList();

    void add(Role role, Integer[] permissionIds, Integer[] menuIds);

    Role findById(Integer id);

    List<Integer> findpermissionIds(Integer id);

    void edit(Role role, Integer[] permissionIds, Integer[] menuIds);

    void delete(Integer id);

    List<Integer> findMenuIds(Integer id);
}
