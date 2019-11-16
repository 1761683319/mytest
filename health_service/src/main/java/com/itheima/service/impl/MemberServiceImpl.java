package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    /**
     * @Path: com.itheima.service.impl.SetmealServiceImpl
     * @param
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     * @Author: Zzc
     * @Date: 2019/11/15 17:14
     * @Description: 会员男女比例占比饼图
     */
    @Override
    public Map<String, Object> getMemberSexReport() {
        Map<String,Object> rsMap = new HashMap<>();//业务层返回结果
        List<String> MemberSex = new ArrayList<>();//性别名称集合
        List<Map> MemberSexCount = memberDao.getMemberSexReport();//性别对应预约数量

        //计算性别为null的数量
        Integer memberTotalCount = memberDao.findMemberTotalCount();

        Integer sexTotalCount = 0;


        if(MemberSexCount!=null && MemberSexCount.size()>0){
            for (Map map : MemberSexCount) {
                String name = (String)map.get("name");//套餐名称
                MemberSex.add(name);//将套餐预约数量中套餐取出放到setmealNames
                Integer value =  Integer.parseInt((String) map.get("value").toString());
                sexTotalCount = sexTotalCount+value;

               /* String sex = (String)map.get("sex");//性别名称
                if ("1".equals(sex)){
                    MemberSex.add("男");//将套餐预约数量中套餐取出放到MemberSex
                }else if ("2".equals(sex)){
                    MemberSex.add("女");//将套餐预约数量中套餐取出放到MemberSex
                }*/
            }
        }
        //性别为null的数据加入List<Map> MemberSexCount
        Map<String,Object> mapNull = new HashMap<>();

        mapNull.put("value",memberTotalCount-sexTotalCount);
        mapNull.put("name","未设定");

        MemberSexCount.add(mapNull);
        MemberSex.add("未设定");
        //套餐名称
        rsMap.put("MemberSex",MemberSex);
        //套餐对应的预约数量
        rsMap.put("MemberSexCount",MemberSexCount);
        return rsMap;
    }
    /**
     * @Path: com.itheima.service.impl.MemberServiceImpl
     * @param
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     * @Author: Zzc
     * @Date: 2019/11/15 19:31
     * @Description: 年龄段占比饼图
    */
    @Override
    public Map<String, Object> getMemberAgeReport() {
        Map<String,Object> rsMap = new HashMap<>();//业务层返回结果
        List<String> MemberAge = new ArrayList<>();//年龄段名称集合
        List<Map> MemberAgeCount = memberDao.getMemberAgeReport();//年龄段对应预约数量

        //计算性别为null的数量
        Integer memberTotalCount = memberDao.findMemberTotalCount();

        Integer ageTotalCount = 0;


        if(MemberAgeCount!=null && MemberAgeCount.size()>0){
            for (Map map : MemberAgeCount) {
                String name = (String)map.get("name");//套餐名称
                MemberAge.add(name);//将套餐预约数量中套餐取出放到setmealNames
                Integer value =  Integer.parseInt((String) map.get("value").toString());
                ageTotalCount = ageTotalCount+value;

            }
        }
        //性别为null的数据加入List<Map> MemberSexCount
        Map<String,Object> mapNull = new HashMap<>();

        mapNull.put("value",memberTotalCount-ageTotalCount);
        mapNull.put("name","未设定");

        MemberAgeCount.add(mapNull);
        MemberAge.add("未设定");
        //套餐名称
        rsMap.put("MemberAge",MemberAge);
        //套餐对应的预约数量
        rsMap.put("MemberAgeCount",MemberAgeCount);
        return rsMap;
    }
}
