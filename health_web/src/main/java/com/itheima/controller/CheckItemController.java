package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检查项控制层
 *
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    /**
     * 新增检查项
     *
     * @param checkItem
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody CheckItem checkItem) {
        //调用service服务
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }


    /**
     *
     *
     *
     * 检查项分页
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "/findPage", method = RequestMethod.POST)
    //@PreAuthorize("hasAnyAuthority('CHECKITEM_QUERY')")//CHECKITEM_QUERY关键字权限 才可以查询检查项数据
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        //调用service服务
        try {
            PageResult pageResult = checkItemService.findPage(queryPageBean.getPageSize(), queryPageBean.getCurrentPage(), queryPageBean.getQueryString());
            return pageResult;//直接返回分页数据
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除检查项
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('CHECKITEM_DELETE')")  //删除检查项关键字（不能随便写 跟数据库表对应关系）
    public Result deleteByCheckItemId(int id) {
        //调用service服务
        try {
            checkItemService.deleteByCheckItemId(id);
            return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }

    /**
     * 根据检查项id查询检查项数据 CV
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result findById(int id) {
        //调用service服务
        try {
            CheckItem checkItem = checkItemService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }


    /**
     * 修改检查项数据
     * @param checkItem
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result edit(@RequestBody CheckItem checkItem) {
        //调用service服务
        try {
            checkItemService.edit(checkItem);
            return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }

    /**
     * 查询检查项所有数据
     */
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result findAll() {
        //调用service服务
        try {
            List<CheckItem> checkItemList = checkItemService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }


}
