package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.List;
import java.util.Map;

/**
 * @author wangxin
 * @version 1.0
 */
public interface MemberService {
    /**
     * 根据手机号码查询会员
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 自动注册会员
     * @param dbMember
     */
    void add(Member dbMember);

    /**
     * 根据年月查询会员数
     * @param months
     * @return
     */
    List<Integer> findMemberCountBeforeDate(List<String> months);


    /**
     * @Path: com.itheima.controller.ReportController
     * @param
     * @return: com.itheima.entity.Result
     * @Author: Zzc
     * @Date: 2019/11/15 16:56
     * @Description: 会员男女比例占比饼图
     */
    Map<String, Object> getMemberSexReport();
    /**
     * @Path: com.itheima.service.MemberService
     * @param
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     * @Author: Zzc
     * @Date: 2019/11/15 19:31
     * @Description: 年龄段占比饼图
    */
    Map<String, Object> getMemberAgeReport();
}
