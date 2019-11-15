package com.itheima.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 自定义认证和授权类
 * @author wangxin
 * @version 1.0
 */
@Component
public class SpringSecurityService implements UserDetailsService {

    //注入userService
    @Reference
    private UserService userservice;


    /**
     * 认证和授权方法实现
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名查询用户信息（包含角色和权限信息）
        User user = userservice.findByUserName(username);
        //2.判断用户对象是否为空 为空return null
        if(user == null){
            return null;
        }
        //3.不为空 为当前用户授权 遍历角色和权限表授予 权限和角色表中keyword
        Set<Role> roles = user.getRoles();//角色集合
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();//当前用户所有的权限
        if(roles != null && roles.size()>0){
            for (Role role : roles) {
                String keyword = role.getKeyword();//角色关键字
                grantedAuthorityList.add(new SimpleGrantedAuthority(keyword));
                //授权权限关键字
                Set<Permission> permissions = role.getPermissions();
                if(permissions != null && permissions.size()>0){
                    for (Permission permission : permissions) {
                        grantedAuthorityList.add(new SimpleGrantedAuthority(permission.getKeyword()));
                    }
                }

            }
        }
        //4.返回new User(用户名,数据库的密码,数据库中动态查询的权限列表)
        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),grantedAuthorityList);
    }
}
