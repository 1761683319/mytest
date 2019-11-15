package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 检查项接口
 * @author wangxin
 * @version 1.0
 */
public interface CheckItemDao {

    /**
     * 新增检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 检查项分页
     * @param queryString
     * @return
     */
    Page<CheckItem> selectByCondition(String queryString);

    /**
     * 根据检查项id查询中间表数据是否存在
     * @param id
     * @return
     */
    int findCountByCheckItemId(int id);

    /**
     * 根据检查项id删除检查项记录
     * @param id
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
     * 查询所有检查组
     * @return
     */
    List<CheckItem> findAll();
}
