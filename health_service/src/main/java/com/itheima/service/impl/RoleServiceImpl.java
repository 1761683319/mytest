package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.RoleDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/23 15:57
 * @Version V1.0
 */
@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    // 角色
    @Autowired
    private RoleDao roleDao;

    //列表显示
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        PageHelper.startPage(currentPage, pageSize);
        Page<Role> page = roleDao.findPage(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //添加 编辑 第二页的分配权限显示
    @Override
    public List<Permission> findPermissionList() {
        return roleDao.findPermissionList();
    }

    /*添加 角色插入数据以及先setRoleAndPermission传值*/
    @Override
    public void add(Role role, Integer[] permissionIds, Integer[] menuIds) {
        roleDao.add(role);
        setRoleAndPermission(role.getId(), permissionIds);
        setRoleAndMenu(role.getId(), menuIds);
    }


    /* 编辑 查询角色数据 */
    @Override
    public Role findById(Integer id) {
        return roleDao.findById(id);
    }

    /*编辑 向中间表查询已经选中的ids*/
    @Override
    public List<Integer> findpermissionIds(Integer id) {
        return roleDao.findpermissionIds(id);
    }

    @Override
    public void edit(Role role, Integer[] permissionIds,Integer[] menuIds) {
        roleDao.edit(role);
        //先删除中间表的数据
        roleDao.deleteRoleIdByPermissionIds(role.getId());
        roleDao.deleteRoleIdByMenuIds(role.getId());
        //添加中间表数据
        setRoleAndPermission(role.getId(), permissionIds);
        setRoleAndMenu(role.getId(), menuIds);
    }

    /*删除操作*/
    @Override
    public void delete(Integer id) {
        //先查role_permission
        long permissionTotal = roleDao.findRoleIdByPermissionCount(id);
        if (permissionTotal > 0) {
            throw new RuntimeException("当前角色和权限之间存在关联关系，不能删除");
        }

        long menuTotal = roleDao.findRoleIdByMenuCount(id);
        if (menuTotal > 0) {
            throw new RuntimeException("当前角色和菜单之间存在关联关系，不能删除");
        }

        long userTotal = roleDao.findUserByRoleId(id);
        if (userTotal > 0) {
            throw new RuntimeException("当前角色和用户之间存在关联关系，不能删除");
        }

        roleDao.delete(id);
    }

    @Override
    public List<Integer> findMenuIds(Integer id) {
        return roleDao.findMenuIds(id);
    }

    /*添加操作 先中间表插入数据*/
    private void setRoleAndPermission(Integer roleId, Integer[] permissionIds) {
        if (permissionIds != null && permissionIds.length > 0) {
            for (Integer permissionId : permissionIds) {
                // 方案一：在CheckGroupDao中@Param
                // checkGroupDao.addCheckGroupAndCheckItem(roleId,checkItemId);
                // 方案二：使用Map集合
                Map<String, Object> map = new HashMap<>();
                map.put("Role_Id", roleId);
                map.put("permission_Id", permissionId);
                roleDao.addRoleAndPermission(map);
            }
        }
    }

    /*添加操作 向t_role_menu中间表插入数据*/
    private void setRoleAndMenu(Integer id, Integer[] menuIds) {
        if (menuIds != null && menuIds.length > 0) {
            for (Integer menuId : menuIds) {
                Map<String, Object> map = new HashMap<>();
                map.put("Role_Id", id);
                map.put("Menu_Id", menuId);
                roleDao.addRoleAndMenu(map);
            }
        }
    }
}
