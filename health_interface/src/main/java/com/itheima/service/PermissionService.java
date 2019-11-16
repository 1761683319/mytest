package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Permission;

public interface PermissionService {

    PageResult pageQery(String queryString, Integer pageSize, Integer currentPage);

    void add(Permission permission);

    Permission findById(Integer id);

    void edit(Permission permission);

    void delete(Integer id);
}
