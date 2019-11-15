package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName CheckItemController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/23 15:58
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/permission")
public class PermissionController {

    @Reference
    PermissionService permissionService;

    // 获取列表（权限）
    @RequestMapping(value = "/pageQuery")
    public PageResult pageQuery(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = permissionService.pageQery(queryPageBean.getQueryString(), queryPageBean.getPageSize(), queryPageBean.getCurrentPage());

        return pageResult;
    }

    // 添加权限
    @RequestMapping(value = "/add")
    public Result add(@RequestBody Permission permission) {
        try {
            permissionService.add(permission);
            return new Result(true, "添加权限成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加权限失败");
        }
    }

    // @编辑 查询一条数据
    @RequestMapping(value = "/findById")
    public Result findById(Integer id) {
        try {
            Permission permission = permissionService.findById(id);
            return new Result(true, "查询权限成功", permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询权限失败");
        }

    }

    //编辑
    @RequestMapping(value = "/edit")
    public Result edit(@RequestBody Permission permission) {
        try {
            permissionService.edit(permission);
            return new Result(true, "编辑权限成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "编辑权限失败");
        }
    }

    //删除
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            permissionService.delete(id);
        } catch (RuntimeException e) {
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除权限失败");
        }
        return new Result(true, "删除权限成功");
    }

}
