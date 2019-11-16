package com.itheima.jobs;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.DateUtils;

import java.util.Date;

public class ClearOrderSetting {

    @Reference
    private OrderSettingService orderSettingService;

    public void clearOrderSetting() throws Exception {
        System.out.println("start。。。。");
        Date date = new Date();
        String today = DateUtils.parseDate2String(date);//2019-11-11
        System.out.println(today);
        orderSettingService.clearOrderSetting(today);
    }
}
