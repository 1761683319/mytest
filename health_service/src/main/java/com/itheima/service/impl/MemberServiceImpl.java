package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员管理业务层管理
 * @author wangxin
 * @version 1.0
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member dbMember) {
        memberDao.add(dbMember);
    }

    /**
     * 根据年月查询会员数
     * @param months
     * @return
     */
    @Override
    public List<Integer> findMemberCountBeforeDate(List<String> months) {
        List<Integer> memberCount = new ArrayList<>();
        //遍历年月 2018-12          select count(id) from t_member where regTime <= '2018-12-31'
        for (String month : months) {
            String newYearMonthDay = month + "-31";
            Integer monthCount = memberDao.findMemberCountBeforeDate(newYearMonthDay);
            memberCount.add(monthCount);
        }
        return memberCount;
    }
}
