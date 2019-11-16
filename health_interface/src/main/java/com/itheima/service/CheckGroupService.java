package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;

import java.util.List;

/**
 * 检查组接口
 * @author wangxin
 * @version 1.0
 */
public interface CheckGroupService {
    /**
     * 新增检查组
     * @param checkGroup
     * @param checkitemIds
     */
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 检查组分页
     * @param pageSize
     * @param currentPage
     * @param queryString
     * @return
     */
    PageResult findPage(Integer pageSize, Integer currentPage, String queryString);

    /**
     * 根据检查组id查询检查组数据
     * @param id
     * @return
     */
    CheckGroup findById(int id);
    /**
     * 根据检查组id查询检查项ids
     */
    List<Integer> findCheckItemIdsByCheckGroupId(int checkGroupId);
    /**
     * 编辑检查组
     * @param checkGroup  检查组
     * @param checkitemIds 检查项ids
     * @return
     */
    void edit(CheckGroup checkGroup, Integer[] checkitemIds);
    /**
     * 查询所有检查组数据
     * @return
     */
    List<CheckGroup> findAll();
}
