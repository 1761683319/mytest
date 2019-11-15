package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map; /**
 * 预约设置持久层
 * @author wangxin
 * @version 1.0
 */
public interface OrderSettingDao {
    /**
     * 根据预约日期查询是否已经预约设置
     * @param orderDate
     * @return
     */
    int findCountByOrderDate(Date orderDate);

    /**
     * 根据预约日期修改预约人数
     * @param orderSetting
     */
    void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 根据预约日期修改已经预约人数
     * @param orderSetting
     */
    void editReservationsByOrderDate(OrderSetting orderSetting);

    /**
     * 新增预约设置
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 根据起始日期和 结束日期 查询预约设置数据
     * @param map
     * @return
     */
    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);

    /**
     * 根据预约日期查询预约设置记录
     * @param newOrderDate
     * @return
     */
    OrderSetting findByOrderDate(Date newOrderDate);
}
