package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.PermissionDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/23 15:57
 * @Version V1.0
 */
@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {

    // 权限
    @Autowired
    private PermissionDao permissionDao;


    @Override
    public PageResult pageQery(String queryString, Integer pageSize, Integer currentPage) {
        //设置开始
        PageHelper.startPage(currentPage,pageSize);
        // 2：查询
        Page<Permission> page = permissionDao.findPage(queryString);
        // 组织PageResult
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void add(Permission permission) {
        //将关键词进行转换已免是小写的关键词
        String keyword = permission.getKeyword();
       permission.setKeyword(keyword.toUpperCase());
       permissionDao.add(permission);
    }

    @Override
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }

    @Override
    public void edit(Permission permission) {
        permissionDao.edit(permission);
    }

    /*findCheckGroupAndCheckItemCountByCheckItemId*/
    @Override
    public void delete(Integer id) {
       long total = permissionDao.findRoleCountByPermissionsId(id);
       if (total >0){
           throw new RuntimeException("当前权限和角色之间存在关联关系，不能删除");
       }
        permissionDao.delete(id);
    }


}
