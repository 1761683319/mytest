package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查组业务逻辑处理层
 * @author wangxin
 * @version 1.0
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService{

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 新增检查项
     * @param checkGroup
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //1.往检查组表中插入数据
        checkGroupDao.add(checkGroup);
        //2.往中间表插入数据  检查组id  检查项ids
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }

    /**
     * 设置检查组和检查项中间表
     * @param groupId
     * @param checkitemIds
     */
    public void setCheckGroupAndCheckItem(Integer groupId,Integer[] checkitemIds){
        if(checkitemIds != null && checkitemIds.length>0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("groupId",groupId);
                map.put("checkitemId",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }


    /**
     * 检查项分页
     * @param pageSize
     * @param currentPage
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer pageSize, Integer currentPage, String queryString) {
        //设置分页参数
        PageHelper.startPage(currentPage,pageSize);
        //紧跟着分页参数代码 需要分页的查询语句 （中间不能有任何代码）
        Page<CheckGroup> checkGroupPage = checkGroupDao.selectByCondition(queryString);
        return new PageResult(checkGroupPage.getTotal(),checkGroupPage.getResult());
    }
    /**
     * 根据检查组id查询检查组数据
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(int id) {
        return checkGroupDao.findById(id);
    }
    /**
     * 根据检查组id查询检查项ids
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(int checkGroupId) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(checkGroupId);
    }
    /**
     * 编辑检查组
     * @param checkGroup  检查组
     * @param checkitemIds 检查项ids
     * @return
     */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //检查组id
        Integer groupId = checkGroup.getId();
        //更新检查组数据
        checkGroupDao.edit(checkGroup);
        //根据检查组id 清空检查项数据
        checkGroupDao.deleteRelationByCheckGroupId(groupId);
        //根据检查组id 和 检查项ids 重新建立关系
        setCheckGroupAndCheckItem(groupId,checkitemIds);

    }
    /**
     * 查询所有检查组数据
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

}
