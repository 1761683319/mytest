package com.itheima.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hello")
public class HelloController {
    /**
     * @PreAuthorize("hasAuthority('add')"):表达式 注解方式配置权限控制
     * @return
     */
    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('update')")//表示用户必须拥有add权限才能调用当前方法
    public String update(){
        System.out.println("update...");
        return null;
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")//表示用户必须拥有ROLE_ADMIN角色才能调用当前方法
    public String delete(){
        System.out.println("delete...");
        return null;
    }
}