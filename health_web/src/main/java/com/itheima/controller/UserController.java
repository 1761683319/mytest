package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Role;
import com.itheima.service.MenuService;
import com.itheima.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户控制层
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private MenuService menuService;

    @Reference
    UserService userService;

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


    @RequestMapping(value = "/getRole",method = RequestMethod.GET)
    public Result getRole(){
        //获取当前的登陆用户名
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //根据名字查询左边菜单的内容
            //调用业务层的方法查询List<menu>,并且返回结果
            String name =user.getUsername();
            List<Menu> menuList=menuService.findMenu(name);
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,menuList);
        } catch (Exception e) {
            return new Result(false,MessageConstant.GET_USERNAME_FAIL);
        }
    }

    // 用户列表显示
    @RequestMapping(value = "/pageQuery")
    public PageResult pageQuery(@RequestBody QueryPageBean queryPageBean) {

        PageResult pageResult = userService.pageQuery(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString()
        );
        return pageResult;
    }

    @RequestMapping(value = "findRoleList")
    public Result findRoleList() {
        try {
            List<Role> role = userService.findRoleList();
            return new Result(true, "查询角色成功", role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询角色失败");
        }
    }

    @RequestMapping(value = "/add")
    public Result add(@RequestBody com.itheima.pojo.User user, Integer[] roleIds) {
        try {
            userService.add(user, roleIds);
            return new Result(true, "添加用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加用户失败");
        }
    }

    @RequestMapping(value = "/findById")
    public Result findById(Integer id) {
        try {
            com.itheima.pojo.User user = userService.findById(id);
            return new Result(true, "用户查询成功", user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "用户查询失败");
        }

    }

    @RequestMapping(value = "/findroleIds")
    public List<Integer> findroleIds(Integer id) {
        try {
            List<Integer> roleIds = userService.findroleIds(id);
            return roleIds;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/edit")
    public Result edit(@RequestBody com.itheima.pojo.User user, Integer[] roleIds) {
        try {
            userService.edit(user, roleIds);
            return new Result(true, "编辑用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "编辑用户失败");
        }
    }

    @RequestMapping(value = "/delete")
    public Result delete(Integer id) {
        try {
            userService.delete(id);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除用户失败");
        }
        return new Result(true, "删除用户成功");
    }

}
