package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName CheckItemController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/23 15:58
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/role")
public class RoleController {

    @Reference
    RoleService roleService;

    // 获取用户信息（用户名）
    @RequestMapping(value = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        //列表
        try {
            PageResult pageResult = roleService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*添加 分配权限显示*/
    @RequestMapping(value = "/findPermissionList")
    public Result findPermissionList() {

        try {
            List<Permission> permissions = roleService.findPermissionList();
            return new Result(true, "查询分配权限列表成功", permissions);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询分配权限列表失败");
        }
    }

    /*添加 role以及角色权限中间表*/
    @RequestMapping(value = "/add")
    public Result add(@RequestBody Role role, @RequestParam(value = "permissionIds") Integer[] permissionIds, @RequestParam(value = "menuIds") Integer[] menuIds) {
        try {
            roleService.add(role, permissionIds, menuIds);
            return new Result(true, "添加角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加角色失败");
        }
    }

    /*编辑回显 查询角色数据*/
    @RequestMapping(value = "/findById")
    public Result findById(Integer id) {
        try {
            Role role = roleService.findById(id);
            return new Result(true, "查询角色成功", role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询角色失败");
        }
    }

    /*编辑回显 查询中间表已经选中的数据*/
    @RequestMapping("findpermissionIds")
    public List<Integer> findpermissionIds(Integer id) {
        try {
            List<Integer> ids = roleService.findpermissionIds(id);
            return ids;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*编辑回显 查菜单中间表已经选中的数据*/
    @RequestMapping(value = "/findMenuIds")
    public List<Integer> findMenuIds(Integer id) {
        try {
            List<Integer> ids = roleService.findMenuIds(id);
            return ids;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*编辑更新操作*/
    @RequestMapping("/edit")
    public Result edit(@RequestBody Role role, @RequestParam(value = "permissionIds") Integer[] permissionIds, @RequestParam(value = "menuIds") Integer[] menuIds) {
        try {
            roleService.edit(role, permissionIds, menuIds);
            return new Result(true, "编辑角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "编辑角色失败");
        }
    }

    /*删除操作*/
    @RequestMapping(value = "/delete")
    public Result delete(Integer id) {
        try {

            roleService.delete(id);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除角色失败");
        }
        return new Result(true, "删除角色成功");
    }

}
