package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查项业务逻辑处理层
 * @author wangxin
 * @version 1.0
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService{

    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 新增检查项
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 检查项分页
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
        Page<CheckItem> checkItemPage = checkItemDao.selectByCondition(queryString);
        return new PageResult(checkItemPage.getTotal(),checkItemPage.getResult());
    }
    /**
     * 删除检查项
     */
    @Override
    public void deleteByCheckItemId(int id) {
        //1.根据检查项id 到中间表中查询 t_checkgroup_checkitem
        int count = checkItemDao.findCountByCheckItemId(id);
        //2.存在 跟检查组关系
        if(count>0){
            //3.提示用户不能删除此检查项
            throw new RuntimeException("此检查项已经关联检查组，不能删除");
        }
        //4.不存在检查组关系 直接删除检查项记录
        checkItemDao.deleteByCheckItemId(id);
    }
    /**
     * 根据检查项id查询检查项数据
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById(id);
    }

    /**
     * 编辑检查项
     * @param checkItem
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
