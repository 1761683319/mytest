package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制层
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 从springSecurity框架容器中获取用户信息
     *
     * 注意：
     *    容器中的用户信息怎么来的？
     *    认证后springSecurity框架将认证成功后的用户信息放到容器中。
     */
    @RequestMapping(value = "/findUserName",method = RequestMethod.GET)
    public Result findUserName(){
        //SecurityContext:容器对象
        SecurityContext context = SecurityContextHolder.getContext();
        //authentication:认证对象（登录后的信息）
        Authentication authentication = context.getAuthentication();
        //String username = authentication.getName();
        User user = (User) authentication.getPrincipal();//用户对象
        String username = user.getUsername();
        //String password = user.getPassword();
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
    }
}
