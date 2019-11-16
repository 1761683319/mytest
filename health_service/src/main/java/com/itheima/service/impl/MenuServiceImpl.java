package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.MenuDao;
import com.itheima.dao.RoleDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
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
        List<Menu> menu = menuDao.findMenuOne(username);
        if (!menu.isEmpty() || menu.size() > 0) {
            for (Menu menu1 : menu) {
                //第二个查询子菜单的数据,如果父菜单id不为空，则查询子菜单，否则返回null
                if (menu1.getIcon() != null) {
                    Integer id = menu1.getId();
                    List<Menu> menuSons = menuDao.finMenuSon(id);
                    menu1.setChildren(menuSons);
                }
                //根据名字查询权限
                Set<Role> roles = roleDao.findRoles(username);
                menu1.setRoles(roles);
            }
        }
        return menu;
    }

    @Override
    /*
     * 根据用户名查询整理用户可操作菜单
     * */
    public List<Map<String, Object>> getUserMenuByUsername(String username) {
        List<Map<String, Object>> userMenuList = menuDao.getUserMenuByUsername(username);
        return this.getMenuTree(null, userMenuList);
    }

    /*
     * 将菜单信息整理成菜单树结构
     * @param parentId 父节点id, null表示顶级节点
     * @param lists 用户所拥有的菜单列表
     * */
    public List<Map<String, Object>> getMenuTree(Integer parentId, List<Map<String, Object>> lists) {
        List<Map<String, Object>> userMenuList = new ArrayList<>();
        //获取顶层节点
        for (Map<String, Object> map : lists) {
            if (map.get("parentMenuId") == parentId) {
                userMenuList.add(map);
            }
        }
        //出口
        if (userMenuList.size() == 0) {
            return null;
        }
        // //对节点排序
        // Collections.sort(userMenuList, Comparator.comparingInt(Menu::getPriority));

        //对节点排序
        Collections.sort(userMenuList, (Map m1, Map m2) -> {
            int a = (int) m1.get("priority");
            int b = (int) m2.get("priority");
            return a - b;
            //return (int)m1.get("priority") - (int)m2.get("priority");
        });
        //获取顶层的子节点
        for (Map<String, Object> map : userMenuList) {
            map.put("children", this.getMenuTree((Integer) map.get("id"), lists));
        }
        return userMenuList;
    }

    @Override
    /*
     * 查询菜单分页数据
     * */
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Map> page = menuDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    /*
     * 获取顶级菜单
     * */
    public List<Menu> getLevelOneMenu() {
        return menuDao.getLevelOneMenu();
    }

    @Override
    /*
     * 添加菜单
     * */
    public void add(Menu menu) {
        //设置level
        menu.setLevel(menu.getParentMenuId() == null ? 1 : 2);
        //处理优先级priority, 先查询是否有该优先级, 没有就直接加, 有就将>=的加1, 再设置自己
        if (menu.getPriority() != null) {
            int isExistPriority = menuDao.isExistPriority(menu.getParentMenuId(), menu.getPriority());
            if (isExistPriority > 0) {
                //存在,需要处理,  不存在就使用该优先级
                if (menu.getParentMenuId() == null) {
                    //将>=该优先级的priority+1, path不改
                    menuDao.updateOneLevelPriorityAfterNumber(menu.getPriority());
                } else {//二级节点需要修改path
                    //获取父节点的path, 然后修改其他的path
                    Menu parent = menuDao.findMenuById(menu.getParentMenuId());
                    menuDao.updateTwoLevelPriorityAfterNumber(menu.getParentMenuId(), parent.getPath(), menu.getPriority());
                }
            }
        } else {//前端没有传优先级,设置为最末尾
            //获取最大的优先级数
            try {
                int maxPriority = menuDao.getMaxPriorityByParentId(menu.getParentMenuId());
                menu.setPriority(maxPriority + 1);
            } catch (Exception e) {
                //当没有值时报错, 优先级就是第一, 走这里
                menu.setPriority(1);
            }
        }
        //拼接path
        if (menu.getParentMenuId() == null) {//顶级菜单
            //获取path最大的顶级菜单
            Menu maxPathMenu = menuDao.findLevelOneMaxPathMenu();
            int path = Integer.parseInt(maxPathMenu.getPath()) + 1;
            menu.setPath(path + "");
        } else {//二级菜单
            Menu parentMenu = menuDao.findMenuById(menu.getParentMenuId());
            menu.setPath("/" + parentMenu.getPath() + "-" + menu.getPriority());
        }
        //执行添加
        menuDao.add(menu);

    }

    /*
     * 查询所有菜单, 整理成菜单树
     * */
    public List<Map<String, Object>> getAllMenuTree() {
        List<Map<String, Object>> allMenuList = menuDao.findAll();
        return this.getMenuTree(null, allMenuList);
    }

    @Override
    public Menu findById(Integer id) {
        return menuDao.findMenuById(id);
    }

    @Override
    public void edit(Menu menu) {
        //获取该菜单数据库中的信息
        Menu menuSelf = menuDao.findMenuById(menu.getId());
        if (menuSelf.getParentMenuId() == menu.getParentMenuId()) {
            if (menuSelf.getPriority() == menu.getPriority()) {
                //如果parentMenuId和priority没改, 就直接update
                menuDao.edit(menu);
                return;
            } else {//更改了自己的优先级
                //首先将priority>=menu.getPriority() and priority != menu.getPriority() and parentMenuId = menu.getParentMenuId
                //的priority加1, 再将自己的priority设为menu.getPriority()
                //处理优先级priority, 先查询是否有该优先级, 没有就直接加, 有就将>=的加1, 再设置自己
                if (menu.getPriority() != null) {
                    int isExistPriority = menuDao.isExistPriority(menu.getParentMenuId(), menu.getPriority());
                    if (isExistPriority > 0) {
                        //存在,需要处理,  不存在就使用该优先级
                        if (menu.getParentMenuId() == null) {
                            //将>=该优先级的priority+1, path不改
                            menuDao.updateOneLevelPriorityAfterNumber(menu.getPriority());
                        } else {//二级节点需要修改path
                            //获取父节点的path, 然后修改其他的path
                            Menu parentMenu = menuDao.findMenuById(menu.getParentMenuId());
                            //menuDao.updateTwoLevelPriorityAfterNumber(menu.getParentMenuId(), parentMenu.getPath(), menu.getPriority());
                            //将>=该优先级的获取出来循环修改,priority+1, path也要改
                            List<Menu> sonMenus = menuDao.findSonMenuByIdAndAfterEqualPriority(parentMenu.getId(), menu.getPriority());
                            if (sonMenus != null && sonMenus.size() > 0) {
                                for (Menu sonMenu : sonMenus) {
                                    //level不用改,只需改优先级和path
                                    sonMenu.setPriority(menu.getPriority() + 1);
                                    try {
                                        Integer.parseInt(parentMenu.getPath());//不报错说明是一级菜单
                                        sonMenu.setPath("/" + parentMenu.getPath() + "-" + sonMenu.getPriority());
                                    } catch (Exception e) {
                                        sonMenu.setPath(parentMenu.getPath() + parentMenu.getPath() + "-" + sonMenu.getPriority());
                                    }
                                    //修改自己
                                    menuDao.edit(menu);
                                    //修改子菜单的path
                                    this.dealwithSonMenuPath(sonMenu.getId(), sonMenu.getPath());
                                }
                            }
                        }
                    }
                } else {//前端没有传优先级,设置为最末尾
                    //获取最大的优先级数
                    try {
                        int maxPriority = menuDao.getMaxPriorityByParentId(menu.getParentMenuId());
                        menu.setPriority(maxPriority + 1);
                    } catch (Exception e) {
                        //当没有值时报错, 优先级就是第一, 走这里
                        menu.setPriority(1);
                    }
                }
                //拼接path
                if (menu.getParentMenuId() == null) {//顶级菜单
                    //获取path最大的顶级菜单
                    Menu maxPathMenu = menuDao.findLevelOneMaxPathMenu();
                    int path = Integer.parseInt(maxPathMenu.getPath()) + 1;
                    menu.setPath(path + "");
                } else {//二级菜单
                    Menu parentMenu = menuDao.findMenuById(menu.getParentMenuId());
                    try {
                        Integer.parseInt(parentMenu.getPath());
                        menu.setPath("/" + parentMenu.getPath() + "-" + menu.getPriority());
                    } catch (Exception e) {
                        String path = parentMenu.getPath() + parentMenu.getPath().substring(parentMenu.getPath().lastIndexOf("/")) + "-" + menu.getPriority();
                        menu.setPath(path);
                    }
                }
                menuDao.edit(menu);
                //处理子菜单path
                this.dealwithSonMenuPath(menu.getId(), menu.getPath());
                return;
            }
        }
        //如果修改了上级菜单, 修改为顶级菜单或者二级菜单
        if (menu.getParentMenuId() == null) {//其他级别菜单修改为顶级菜单
            menu.setLevel(1);//层级修改为1
            //处理优先级
            if (menu.getPriority() == null) {//没有传优先级则设置为最后
                //获取一级菜单最大的优先级
                try {
                    int maxPriority = menuDao.getMaxPriorityByParentId(menu.getParentMenuId());
                    menu.setPriority(maxPriority + 1);
                } catch (Exception e) {
                    //当没有值时报错, 走这里优先级设置为1
                    menu.setPriority(1);
                }
            } else {//传了优先级priority
                int isExistPriority = menuDao.isExistPriority(menu.getParentMenuId(), menu.getPriority());
                if (isExistPriority > 0) {
                    //存在,需要处理,  不存在就使用该优先级
                    //将>=该优先级的priority+1, path不改
                    menuDao.updateOneLevelPriorityAfterNumber(menu.getPriority());
                }
            }
            //处理path
            //设置自己的path
            Menu maxPathMenu = menuDao.findLevelOneMaxPathMenu();//获取path最大的顶级菜单
            int path = Integer.parseInt(maxPathMenu.getPath()) + 1;
            menu.setPath(path + "");
            //处理子菜单的path,子菜单的优先级不用修改
            this.dealwithSonMenu(menu.getId(), menu.getPath(), menu.getLevel());
            //更新自己
            menuDao.edit(menu);
            return;
        }

        if (menu.getParentMenuId() != null) {//其他菜单修改为其他菜单 或者 顶级菜单修改为其他菜单
            Menu parentMenu = menuDao.findMenuById(menu.getParentMenuId());//获取上一级的menu, 用来设置level
            menu.setLevel(parentMenu.getLevel() + 1);//level加1
            //优先级处理
            if (menu.getPriority() == null) {//没有传优先级则设置为最后
                //获取该parentMenuId下最大的优先级
                try {
                    int maxPriority = menuDao.getMaxPriorityByParentId(menu.getParentMenuId());
                    menu.setPriority(maxPriority + 1);
                } catch (Exception e) {
                    //当没有值时报错, 走这里优先级设置为1
                    menu.setPriority(1);
                }
            } else {//传了优先级priority
                int isExistPriority = 0;
                try {
                    isExistPriority = menuDao.isExistPriority(menu.getParentMenuId(), menu.getPriority());
                } catch (Exception e) {//没有子菜单会报错
                    //没有子菜单不用处理
                }
                if (isExistPriority > 0) {
                    //存在,需要处理,  不存在就使用该优先级
                    //将>=该优先级的获取出来循环修改,priority+1, path也要改
                    List<Menu> sonMenus = menuDao.findSonMenuByIdAndAfterEqualPriority(parentMenu.getId(), menu.getPriority());
                    for (Menu sonMenu : sonMenus) {
                        //level不用改,只需改优先级和path
                        menu.setPriority(menu.getPriority() + 1);
                        try {
                            Integer.parseInt(parentMenu.getPath());//不报错说明是一级菜单
                            menu.setPath("/" + parentMenu.getPath() + "-" + menu.getPriority());
                        } catch (Exception e) {
                            menu.setPath(parentMenu.getPath() + parentMenu.getPath() + "-" + menu.getPriority());
                        }
                        //修改自己
                        menuDao.edit(menu);
                        //修改子菜单的path
                        this.dealwithSonMenuPath(menu.getId(), menu.getPath());
                    }
                }
            }
            //处理path
            //设置自己的path
            String path = "";
            try {
                Integer.parseInt(parentMenu.getPath());//不报错, 说明path是数字
                path = "/" + parentMenu.getPath() + "-" + menu.getPriority();
            } catch (Exception e) {//报错说明路径有斜杠/
                path = parentMenu.getPath() + parentMenu.getPath() + "-" + menu.getPriority();
            }
            menu.setPath(path);
            //处理子菜单的path,子菜单的优先级不用修改
            this.dealwithSonMenu(menu.getId(), menu.getPath(), menu.getLevel());
            //更新自己
            menuDao.edit(menu);
            return;
        }
    }

    @Override
    //删除菜单
    public void deleteById(Integer id) {
        //如果存在子菜单, 不能删
        List<Menu> menus = menuDao.findSonMenuById(id);
        if (menus != null && menus.size() > 0) {
            throw new RuntimeException("存在子菜单, 请先删除子菜单后再操作");
        }
        //如果t_role_menu里有关联, 不能删
        int count = menuDao.findCountByMenuId(id);
        if (count > 0) {
            throw new RuntimeException("该菜单有关联角色, 请先解除关联后再操作");
        }
        //执行删除
        menuDao.deleteById(id);
    }

    /*
     * 处理子菜单path和level
     * */
    public void dealwithSonMenu(Integer id, String path, int level) {
        List<Menu> sonMenuList = menuDao.findSonMenuById(id);
        //出口
        if (sonMenuList == null || sonMenuList.size() < 1) {
            return;
        }
        for (Menu menu : sonMenuList) {
            menu.setLevel(level + 1);
            try {
                int path2int = Integer.parseInt(path);
                menu.setPath("/" + path + "-" + menu.getPriority());
            } catch (Exception e) {//抛异常表示不是一级菜单
                String sonPath = path.substring(path.lastIndexOf("/"));
                menu.setPath(path + sonPath + "-" + menu.getPriority());
            }
            menuDao.edit(menu);
            //处理子菜单path
            this.dealwithSonMenu(menu.getId(), menu.getPath(), menu.getLevel());
        }
    }

    /*
     * 处理子菜单path
     * */
    public void dealwithSonMenuPath(Integer id, String path) {
        List<Menu> sonMenuList = menuDao.findSonMenuById(id);
        //出口
        if (sonMenuList == null || sonMenuList.size() < 1) {
            return;
        }
        for (Menu menu : sonMenuList) {
            try {
                int path2int = Integer.parseInt(path);
                menu.setPath("/" + path + "-" + menu.getPriority());
            } catch (Exception e) {//抛异常表示不是一级菜单
                String sonPath = path.substring(path.lastIndexOf("/"));
                menu.setPath(path + sonPath + "-" + menu.getPriority());
            }
            menuDao.edit(menu);
            //处理子菜单path
            this.dealwithSonMenuPath(menu.getId(), menu.getPath());
        }
    }
}
