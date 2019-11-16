package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * 预约设置-接口层
 *
 * @author wangxin
 * @version 1.0
 */
public interface OrderSettingService {
        /**
         * 批量预约设置
         *
         * @param orderSettingList
         */
        void add(List<OrderSetting> orderSettingList);

        /**
         * 根据年月 获取日历组件 预约设置数据
         *
         * @param date
         * @return
         */
        List<Map> getOrderSettingByMonth(String date);

        /**
         * 单个预约设置 （根据日期和预约人数）
         */
        void editNumberByDate(OrderSetting orderSetting);

        void clearOrderSetting(String today);
}
