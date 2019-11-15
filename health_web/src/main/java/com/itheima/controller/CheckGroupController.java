package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import com.itheima.service.CheckItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检查组控制层
 *
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 新增检查组
     *
     * @param checkGroup  检查组
     * @param  checkitemIds [1,2,3,4,5] springmvc自动转成数组
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds) {
        //调用service服务
        try {
            checkGroupService.add(checkGroup,checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }


    /**
     * 检查项分页
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "/findPage", method = RequestMethod.POST)
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        //调用service服务
        try {
            PageResult pageResult = checkGroupService.findPage(queryPageBean.getPageSize(), queryPageBean.getCurrentPage(), queryPageBean.getQueryString());
            return pageResult;//直接返回分页数据
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据检查组id查询检查组数据
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result findById(int id) {
        //调用service服务
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 根据检查组id查询检查项ids
     */
    @RequestMapping(value = "/findCheckItemIdsByCheckGroupId", method = RequestMethod.GET)
    public Result findCheckItemIdsByCheckGroupId(int checkGroupId) {
        //调用service服务
        try {
            List<Integer> checkItemIds = checkGroupService.findCheckItemIdsByCheckGroupId(checkGroupId);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /*@RequestMapping(value = "/findCheckItemIdsByCheckGroupId", method = RequestMethod.GET)
    public List<Integer> findCheckItemIdsByCheckGroupId(int checkGroupId) {
        //调用service服务
        try {
            List<Integer> checkItemIds = checkGroupService.findCheckItemIdsByCheckGroupId(checkGroupId);
            return checkItemIds;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    /**
     * 编辑检查组
     * @param checkGroup  检查组
     * @param checkitemIds 检查项ids
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds) {
        try {
            checkGroupService.edit(checkGroup,checkitemIds);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }



    /**
     * 查询所有检查组数据
     * @return
     */
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result findAll() {
        //调用service服务
        try {
            List<CheckGroup> listCheckGroup = checkGroupService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, listCheckGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

}
