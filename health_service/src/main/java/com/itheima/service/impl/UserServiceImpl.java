package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

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

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<User> page = userDao.pageQuery(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /*添加 编辑 第二项的数据*/
    @Override
    public List<Role> findRoleList() {
        return userDao.findRoleList();
    }

    /*添加操作*/
    @Override
    public void add(User user, Integer[] roleIds) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pasword = passwordEncoder.encode("123");
        user.setPassword(pasword);
        userDao.add(user);
        setUserAndRole(user.getId(), roleIds);
    }

    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public List<Integer> findroleIds(Integer id) {
        return userDao.findroleIds(id);
    }

    @Override
    public void edit(User user, Integer[] roleIds) {
        userDao.edit(user);
        userDao.deleteUserIdByRoleIds(user.getId());
        setUserAndRole(user.getId(), roleIds);
    }

    @Override
    public void delete(Integer id) {
        long Total = userDao.findRoleCountByUserId(id);
        if (Total>0){
            throw new RuntimeException("当前用户和角色之间存在关联关系，不能删除");
        }
        userDao.delete(id);

    }

    private void setUserAndRole(Integer userId, Integer[] RoleIds) {
        if (RoleIds != null && RoleIds.length > 0) {
            for (Integer permissionId : RoleIds) {
                // 方案一：在CheckGroupDao中@Param
                // checkGroupDao.addCheckGroupAndCheckItem(roleId,checkItemId);
                // 方案二：使用Map集合
                Map<String, Object> map = new HashMap<>();
                map.put("User_Id", userId);
                map.put("Role_Id", permissionId);
                userDao.addUserAndRole(map);
            }
        }
    }
}
