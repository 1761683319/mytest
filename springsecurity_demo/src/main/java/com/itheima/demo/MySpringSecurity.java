package com.itheima.demo;

import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 自定义认证（登录）和授权（认证成功后 就给用户授权）类
 * @author wangxin
 * @version 1.0
 */
public class MySpringSecurity implements UserDetailsService
{

    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * 根据用户名 查询用户对象
     * return 返回User对象（不是pojo工程中User对象）
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名查询用户对象
        User userByUserName = getUserByUserName(username);

        //2.判断用户如果为null return null(认证失败-说明账号不存在)
        if(userByUserName == null){
            return null;
        }
        //3.给认证用户授权(写死)
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        //以ROLE_ 叫角色权限关键字  角色表中keyword
        //以非ROLE_开头叫普通权限关键字 权限表中keywrod
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        grantedAuthorityList.add(new SimpleGrantedAuthority("add"));//后续跟权限控制对应关系 （跟数据库中的一致 不能随便写）

        //4.return user（框架的用户对象） 根据用户输入的密码和从数据库查询的密码对比（springsecurity框架实现）
        //将数据库的密码交给框架即可
        //String username, String password,
       // Collection<? extends GrantedAuthority> authorities
        return new org.springframework.security.core.userdetails.User(username,userByUserName.getPassword(),grantedAuthorityList);
    }

    /**
     * 模拟根据用户名从数据库查询用户对象
     */
    public User getUserByUserName(String username){

        if("admin".equals(username)){
            User user = new User();
            user.setUsername(username);
            user.setPassword(encoder.encode("123456"));//盐不用指定 随机生成盐 融合到加密后的密码中了
            return user;
        }
        return null;
    }
}
