package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.List;

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

    
}
