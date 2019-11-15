package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 体检预约业务逻辑层处理
 * @author wangxin
 * @version 1.0
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    /**
     * 体检预约
     * //{"setmealId":"12","sex":"1","name":"111","telephone":"133331111",
     // "validateCode":"1111","idCard":"512501197203035172","orderDate":"2019-11-09"}
     * @param map
     * @return
     */
    @Override
    public Result submitOrder(Map map) throws Exception {
        String orderDate = (String)map.get("orderDate");
        String telephone = (String)map.get("telephone");
        String orderType = (String)map.get("orderType");
        String name = (String)map.get("name");
        String sex = (String)map.get("sex");
        String idCard = (String)map.get("idCard");

        Integer setmealId = Integer.parseInt(map.get("setmealId").toString());

        //将预约日期从字符串转Date
        Date newOrderDate = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(newOrderDate); //将日期字符串转日期对象
        //1.判断当前的日期是否可以预约
                //t_ordersetting
        //a.当前预约日期是否存在记录 select * from t_ordersetting where orderDate = ‘yyyy-mm-dd’
        if(orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //b.是否约满reservation>=number
        if(orderSetting.getReservations()>=orderSetting.getNumber()){
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //2.判断是否是会员
        //根据手机号码1333222333 到t_member查询记录是否存在
        Member dbMember = memberDao.findByTelephone(telephone);
        //是会员
        //防止重复预约？根据 会员id+预约日期+套餐id 到 t_order预约表
        if(dbMember != null){
            Order order = new Order();
            order.setMemberId(dbMember.getId());//会员id
            order.setOrderDate(newOrderDate);//预约日期
            order.setSetmealId(setmealId);//套餐id
            List<Order> listOrder = orderDao.findByCondition(order);
            if(listOrder != null && listOrder.size()>0){
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }
        else
        {
            //不是会员
            //      自动注册会员
            //id name sex idCard phoneNumber regTime
            //没有的信息 后续个人信息维护
            dbMember = new Member();
            dbMember.setName(name);//用户名
            dbMember.setSex(sex);//性别
            dbMember.setIdCard(idCard);//身份证号码
            dbMember.setPhoneNumber(telephone);//手机号码
            dbMember.setRegTime(new Date());//创建用户时间
            memberDao.add(dbMember);
        }
        Integer memberId = dbMember.getId();//获取会员id

        //3.进行预约
                //向t_order表插入一条记录
        Order order = new Order();
        order.setMemberId(memberId);//会员id
        order.setOrderDate(newOrderDate);//预约日期
        order.setOrderType(orderType);//预约类型
        order.setOrderStatus(Order.ORDERSTATUS_NO);//默认未到诊
        order.setSetmealId(setmealId);//套餐id
        orderDao.add(order);

        //id:主键自增 memberId
        //t_ordersetting表里面预约的人数+1
        orderSetting.setReservations(orderSetting.getReservations()+1);//每次预约人数+1
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        //5.跳转预约通知成功页面 展示预约成功数据
        //后台：order.getId() 前端：response.data.data
        //后台：order  前端：response.data.data.id
        return new Result(true, MessageConstant.ORDER_SUCCESS,order);
    }

    /**
     * 预约成功页面
     * @param id
     * @return
     */
    @Override
    public Map findById4Detail(Integer id) {
        Map map = orderDao.findById4Detail(id);
        //Object map1 = map.get("orderDate");
        //将map1 转格式
        //map.put("orderDate",xxxx);
        return map;
    }
}
