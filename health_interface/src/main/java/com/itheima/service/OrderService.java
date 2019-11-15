package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map; /**
 * 体检预约接口
 * @author wangxin
 * @version 1.0
 */
public interface OrderService {
    Result submitOrder(Map map) throws Exception;

    /**
     * 预约成功页面
     * @param id
     * @return
     */
    Map findById4Detail(Integer id);
}
