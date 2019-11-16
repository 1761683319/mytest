package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * 套餐持久层
 * @author wangxin
 * @version 1.0
 */
public interface SetmealDao {
    /**
     * 新增套餐
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 设置套餐和检查组关系
     * @param map
     */
    void setSetmealAndCheckGroup(Map<String, Integer> map);
    /**
     * 套餐分页
     * @param queryString
     * @return
     */
    Page<Setmeal> selectByCondition(String queryString);
    /**
     * 移动端-查询所有套餐列表
     * @return
     */
    List<Setmeal> getSetmeal();

    /**
     * 套餐详情页面数据展示(套餐 检查组 检查项数据)
     * @param id
     * @return
     */
    Setmeal findById(Integer id);

    /**
     * 获取套餐对应的预约数量
     * @return
     */
    List<Map> getSetmealReport();

    /**
     *  获取热门套餐数据
     * @return
     */
    List<Map> hotSetmeal();

}
