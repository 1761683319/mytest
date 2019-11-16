package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 检查项业务层接口
 *
 * @author wangxin
 * @version 1.0
 */
public interface CheckItemService {

    /**
     * 新增检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 检查项分页
     * @param pageSize
     * @param currentPage
     * @param queryString
     * @return
     */
    PageResult findPage(Integer pageSize, Integer currentPage, String queryString);
    /**
     * 删除检查项
     */
    void deleteByCheckItemId(int id);

    /**
     * 根据检查项id查询检查项数据
     * @param id
     * @return
     */
    CheckItem findById(int id);

    /**
     * 编辑检查项
     * @param checkItem
     */
    void edit(CheckItem checkItem);

    /**
     * 查询所有检查项
     * @return
     */
    List<CheckItem> findAll();
}
