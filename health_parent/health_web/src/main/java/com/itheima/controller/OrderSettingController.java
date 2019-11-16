package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置：
 *       批量预约设置
 *          1.下载模板
 *          2.模板中输入预约数据
 *          3.提交模板
 *          4.后台接收预约excel
 *          5.解析excel数据 List<Integer[]>
 *          6.List<String[]>  == > List<OrderSetting>
 *          7.持久化到数据库
 *       单个预约设置
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 批量预约设置
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Result upload(@RequestParam("excelFile")MultipartFile multipartFile){
        try {
            //1.解析Excel
            List<String[]> listStr = POIUtils.readExcel(multipartFile);//将Excel解析转成list<数组>
            //2.List<String[]>  == > List<OrderSetting>
            //定义list<OrderSetting>
            List<OrderSetting> orderSettingList = new ArrayList<>();
            for (String[] str : listStr) {
                OrderSetting orderSetting = new OrderSetting();
                //str:每一行数据
                orderSetting.setOrderDate(new Date(str[0]));//预约日期
                orderSetting.setNumber(Integer.parseInt(str[1]));//预约数量
                orderSettingList.add(orderSetting);
            }
            //3.调用业务层服务持久化到数据库
            orderSettingService.add(orderSettingList);
            return new Result(true, MessageConstant.UPLOAD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.UPLOAD_FAIL);
        }
    }

    /**
     * 接收页面 年月请求返回List<Map>（显示预约设置数据）
     * [ { date: 1, number: 120, reservations: 1 }, { date: 3, number: 120, reservations: 1 }]
     */
    @RequestMapping(value = "/getOrderSettingByMonth",method = RequestMethod.GET)
    public Result getOrderSettingByMonth(String date){
        List<Map> listMap = null;
        try {
            listMap = orderSettingService.getOrderSettingByMonth(date);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,listMap);
    }

    /**
     * 单个预约设置 （根据日期和预约人数）
     */
    @RequestMapping(value = "/editNumberByDate",method = RequestMethod.POST)
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
    }

}
