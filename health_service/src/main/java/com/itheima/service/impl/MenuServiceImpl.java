package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MenuDao;
import com.itheima.dao.RoleDao;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Role;
import com.itheima.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {

    //调用业务层的方法查询
    @Autowired
    private MenuDao menuDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Menu> findMenu(String username) {
        //查询三个数据封装到里面
        List<Menu> menu=menuDao.findMenuOne(username);
        if(!menu.isEmpty()||menu.size()>0){
            for (Menu menu1 : menu) {
                //第二个查询子菜单的数据,如果父菜单id不为空，则查询子菜单，否则返回null
                if(menu1.getParentMenuId()==null){
                    Integer priority=menu1.getPriority();
                    List<Menu> menuSons=menuDao.finMenuSon(priority);
                    menu1.setChildren(menuSons);
                }
                //根据名字查询权限
                Set<Role> roles=roleDao.findRoles(username);
                menu1.setRoles(roles);
            }
        }
        return menu;
    }
}
