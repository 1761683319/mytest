package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.service.MenuService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Reference
    private MenuService menuService;
    /*
    * 获取用户可操作的菜单(用户动态菜单)
    * */
    @RequestMapping("/getMenu")
    public Result getMenu(){
        try {
            //未登录时直接访问将会报错
            //java.lang.ClassCastException: java.lang.String cannot be cast to org.springframework.security.core.userdetails.User
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Map<String, Object>> menuList = menuService.getUserMenuByUsername(user.getUsername());
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, menuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

    /*
    * 菜单分页查询
    * */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return menuService.findPage(queryPageBean);
    }

    /*
    * 获取顶级菜单
    * */
    @RequestMapping("/getLevelOneMenu")
    public Result getLevelOneMenu(){
        try {
            List<Menu> levelOneMenu = menuService.getLevelOneMenu();
            return new Result(true, "获取顶级菜单成功", levelOneMenu);
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(false, "获取顶级菜单失败");
        }
    }

    /*
    * 添加菜单
    * */
    @RequestMapping("/add")
    public Result add(@RequestBody Menu menu){
        try {
            menuService.add(menu);
            return new Result(true, MessageConstant.ADD_MENU_SUCCESS);
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_MENU_FAIL);
        }
    }

    /*
    * 获取所有菜单, 改为菜单树
    * */
    @RequestMapping("/getAllMenuTree")
    public Result getAllMenuTree(){
        try {
            List<Map<String, Object>> menuTree = menuService.getAllMenuTree();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, menuTree);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

    /*
    * 通过id查询menu
    * */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Menu menu = menuService.findById(id);
            return new Result(true, "获取菜单成功", menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "获取菜单失败");
        }
    }

    /*
    * 编辑菜单
    * */
    @RequestMapping("/edit")
    public Result edit(@RequestBody Menu menu){
        try {
            menuService.edit(menu);
            return new Result(true, "编辑菜单成功");
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(false, "编辑菜单失败");
        }
    }

    /*
    * 删除菜单
    * */
    @RequestMapping("/deleteById")
    public Result delete(Integer id){
        try {
            menuService.deleteById(id);
            return new Result(true, "删除菜单成功");
        }catch (RuntimeException r){
            return new Result(false, r.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(false, "删除菜单失败");
        }

    }
}
