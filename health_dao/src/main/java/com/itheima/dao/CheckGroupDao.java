package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map; /**
 * 检查组持久层
 * @author wangxin
 * @version 1.0
 */
public interface CheckGroupDao {
    /**
     * 新增检查组
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 设置检查组和检查项关系
     * @param map
     */
    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    /**
     * 检查组分页
     * @param queryString
     * @return
     */
    Page<CheckGroup> selectByCondition(String queryString);
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
     * 更新检查组数据
     * @param checkGroup
     */
    void edit(CheckGroup checkGroup);

    /**
     * 清空检查项和检查组的关系
     * @param groupId
     */
    void deleteRelationByCheckGroupId(Integer groupId);
    /**
     * 查询所有检查组数据
     * @return
     */
    List<CheckGroup> findAll();
}
