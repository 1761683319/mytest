package com.itheima.dao;

import com.itheima.pojo.Menu;

import java.util.List;

public interface MenuDao {


    List<Menu> finMenuSon(Integer priority);

    List<Menu> findMenuOne(String username);
}
