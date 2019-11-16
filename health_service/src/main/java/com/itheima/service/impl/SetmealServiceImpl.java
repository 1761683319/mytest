package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.MemberDao;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.CheckGroupService;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 套餐业务逻辑处理层
 * @author wangxin
 * @version 1.0
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private MemberDao memberDao;

    /**
     * 新增套餐
     * @param setmeal
     */
    @Override
    public void add(Setmeal setmeal, Integer[] groupIds) {
        //1.往套餐表中插入数据
        setmealDao.add(setmeal);
        //2.往中间表插入数据  套餐id  检查组ids
        setSetmealAndCheckGroup(setmeal.getId(),groupIds);
    }

    /**
     * 套餐分页
     * @param pageSize
     * @param currentPage
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer pageSize, Integer currentPage, String queryString) {
        //设置分页参数
        PageHelper.startPage(currentPage,pageSize);
        //紧跟着分页参数代码 需要分页的查询语句 （中间不能有任何代码）
        Page<Setmeal> setmealPage = setmealDao.selectByCondition(queryString);
        return new PageResult(setmealPage.getTotal(),setmealPage.getResult());
    }
    /**
     * 移动端-查询所有套餐列表
     * @return
     */
    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }
    /**
     * 套餐详情页面数据展示(套餐 检查组 检查项数据)
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
        //方式一：通过代码方式查询
        //1.根据套餐id查询套餐数据
        //2.根据套餐id 通过中间表关联查询检查组表数据  返回List<CheckGroup>
        //3.将List<CheckGroup>  setCheckGroups设置到套餐对象中

        //方式二：通过一个映射配置文件配置调用 另一个映射配置 查询检查组集合 ，将集合数据设置到配置的属性上
        return setmealDao.findById(id);
    }
    /**
     * 获取套餐预约占比饼图
     * setmealNames：List<String>  ['套餐1','套餐2']
     * setmealCount:List<Map> [ {value:335, name:'套餐1'}, {value:310, name:'套餐2'}]
     * @return
     */
    @Override
    public Map<String, Object> getSetmealReport() {
        Map<String,Object> rsMap = new HashMap<>();//业务层返回结果
        List<String> setmealNames = new ArrayList<>();//套餐名称集合
        List<Map> setmealCount = setmealDao.getSetmealReport();//套餐对应预约数量
        if(setmealCount!=null && setmealCount.size()>0){
                for (Map map : setmealCount) {
                    String name = (String)map.get("name");//套餐名称
                    setmealNames.add(name);//将套餐预约数量中套餐取出放到setmealNames
                }
        }
        //套餐名称
        rsMap.put("setmealNames",setmealNames);
        //套餐对应的预约数量
        rsMap.put("setmealCount",setmealCount);
        return rsMap;
    }



    /**
     * 设置套餐和检查组中间表
     * @param setmealId
     * @param groupIds
     */
    public void setSetmealAndCheckGroup(Integer setmealId,Integer[] groupIds){
        if(groupIds != null && groupIds.length>0){
            for (Integer groupId : groupIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("groupId",groupId);
                map.put("setmealId",setmealId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }

}
