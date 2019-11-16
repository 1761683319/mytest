package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MenuDao {


    List<Menu> finMenuSon(Integer priority);

    List<Menu> findMenuOne(String username);

    /*
     * 根据用户名获取用户菜单
     * */
    List<Map<String, Object>> getUserMenuByUsername(String username);

    /*
     * 查询菜单分页数据
     * */
    Page<Map> findPage(String queryString);

    /*
     * 获取顶级菜单
     * */
    List<Menu> getLevelOneMenu();

    /*
     * 查询是否存在该优先级
     * */
    int isExistPriority(@Param("parentMenuId") Integer parentMenuId, @Param("priority") Integer priority);

    /*
     * 将某优先级以后的priority做+1处理
     * */
    void updateOneLevelPriorityAfterNumber(Integer priority);

    /*
     * 通过父id查询优先级的最大值
     * */
    int getMaxPriorityByParentId(Integer parentMenuId);

    /*
     * 通过id查询menu
     * */
    Menu findMenuById(Integer parentMenuId);

    /*
     * 更新二级节点的优先级和路径
     * */
    void updateTwoLevelPriorityAfterNumber(@Param("parentMenuId") Integer parentMenuId, @Param("path") String path, @Param("priority") Integer priority);

    /*
     * 获取path最大的顶级菜单
     * */
    Menu findLevelOneMaxPathMenu();


    /*
     * 添加菜单
     * */
    void add(Menu menu);

    /*
     * 查询所有菜单
     * */
    List<Map<String, Object>> findAll();

    /*
     *  修改菜单
     * */
    void edit(Menu menu);

    /*
     * 通过id查询子菜单
     * */
    List<Menu> findSonMenuById(Integer id);

    /*
     * 根据id查询parentMenuId=id && priority>=#{priority}的menu列表
     * */
    List<Menu> findSonMenuByIdAndAfterEqualPriority(@Param("id") Integer id, @Param("priority") Integer priority);

    /*
     * 查询是否有关联项
     * */
    int findCountByMenuId(Integer id);

    /*
     * 根据id删除menu
     * */
    void deleteById(Integer id);
}
