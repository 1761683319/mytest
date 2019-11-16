package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/appoint")
public  class  appointController {
       @Reference
        private MemberService memberService;

        @RequestMapping("/appointController")
        public Result getMemberReport( String flastTime, String lastTime) {
                Map<String, Object> map = new HashMap<>();
              
                String   flastTime1 =flastTime.substring(1,8);
                String   lastTime1 =lastTime.substring(1,8);
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                List<String> months = new ArrayList<>();
                try {
                        Date startDate1 = sdf.parse(flastTime1);
                        Date endDate1 = sdf.parse(lastTime1);
                        
                        Calendar calendar1 = Calendar.getInstance();
                        Calendar calendar2 = Calendar.getInstance();
                        calendar1.setTime(startDate1);
                        calendar2.setTime(endDate1);
                        months.add(flastTime1);
                        while (calendar1.compareTo(calendar2) < 0) {
                                calendar1.add(Calendar.MONTH, 1);
                                months.add(sdf.format(calendar1.getTime()));
                        }
                        map.put("months", months);
                        //2.根据年月时间 获取每月底累加会员数量
                        List<Integer> memberCount = memberService.findMemberCountBeforeDate(months);
                        
                        
                        map.put("memberCount", memberCount);
                } catch (ParseException e) {
                        e.printStackTrace();
                        return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
                }
                return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        }

}     
            
