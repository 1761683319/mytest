package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberDao {
    public List<Member> findAll();
    public Page<Member> selectByCondition(String queryString);
    public void add(Member member);
    public void deleteById(Integer id);
    public Member findById(Integer id);
    public Member findByTelephone(String telephone);
    public void edit(Member member);
    public Integer findMemberCountBeforeDate(String date);
    public Integer findMemberCountByDate(String date);
    public Integer findMemberCountAfterDate(String date);
    public Integer findMemberTotalCount();
    /**
     * @Path: com.itheima.dao.MemberDao
     * @param
     * @return: java.util.List<java.util.Map>
     * @Author: Zzc
     * @Date: 2019/11/15 17:22
     * @Description:  会员男女比例占比饼图
    */
    List<Map> getMemberSexReport();
    /**
     * @Path: com.itheima.dao.MemberDao
     * @param
     * @return: java.util.List<java.util.Map>
     * @Author: Zzc
     * @Date: 2019/11/15 19:34
     * @Description: 龄段占比饼图
    */
    List<Map> getMemberAgeReport();

}
