package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 用户服务实现
 * @author wangxin
 * @version 1.0
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public User findByUserName(String username) {
        //1.根据用户名查询用户信息
       User user = userDao.findByUserName(username);

        //2.根据用户id查询角色信息
        Integer userId = user.getId();
        //一个用户可以对应多个角色 返回Set<Role>
        Set<Role> roleSet = roleDao.findRoleByUserId(userId);
        if(roleSet != null && roleSet.size()>0){
            //将角色信息设置到用户实体对象中
            user.setRoles(roleSet);
            for (Role role : roleSet) {
                //3.根据角色id查询权限信息
                Set<Permission> permissionSet = permissionDao.findPermissionByRoleId(role.getId());
                //将权限信息设置到角色实体对象中
                role.setPermissions(permissionSet);
            }
        }
        return user;
    }
}
