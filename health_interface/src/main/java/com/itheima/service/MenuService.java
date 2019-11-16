package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Menu;

import java.util.List;
import java.util.Map;

public interface MenuService {
    List<Menu> findMenu(String name);


    /*
     * 通过用户名查询用户可操作菜单
     * */
    List<Map<String, Object>> getUserMenuByUsername(String username);

    /*
     * 查询菜单分页数据
     * */
    PageResult findPage(QueryPageBean queryPageBean);

    /*
     * 获取顶级菜单
     * */
    List<Menu> getLevelOneMenu();

    /*
     * 添加菜单
     * */
    void add(Menu menu);

    /*
     * 获取菜单树
     * */
    List<Map<String, Object>> getAllMenuTree();

    /*
     * 根据id查询菜单
     * */
    Menu findById(Integer id);

    /*
     * 编辑菜单
     * */
    void edit(Menu menu);

    /*
     * 删除菜单
     * */
    void deleteById(Integer id);
}
