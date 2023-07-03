package com.chen.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common.constant.SystemConstants;
import com.chen.common.utils.StreamUtils;
import com.chen.common.utils.TreeBuildUtils;
import com.chen.model.entity.system.SysMenu;
import com.chen.model.entity.system.SysRoleMenu;
import com.chen.model.vo.system.RouterVo;
import com.chen.service.helper.LoginHelper;
import com.chen.system.mapper.SysMenuMapper;
import com.chen.system.mapper.SysRoleMenuMapper;
import com.chen.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Pipiing
 * @description 针对表【sys_menu(菜单表)】的数据库操作Service实现
 * @createDate 2023-06-01 09:50:55
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
        implements ISysMenuService {

    @Autowired
    private SysMenuMapper baseMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> perms = baseMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StrUtil.isNotEmpty(perm)) {
                permsSet.addAll(StrUtil.split(perm.trim(), ","));
            }
        }
        return permsSet;
    }

    @Override
    public List<SysMenu> selectMenuTreeByUserId(Long userId) {
        List<SysMenu> menus = null;
        if (LoginHelper.isAdmin(userId)) {
            // 管理员，拥有所有菜单权限
            menus = baseMapper.selectMenuTreeAll();
        } else {
            // 不是管理员，根据用户ID查询菜单权限
            menus = baseMapper.selectMenuTreeByUserId(userId);
        }
        return this.getChildPerms(menus, 0L);
    }

    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        // TODO 建立前端路由菜单，使用 menu信息 构建 routerVo
        List<RouterVo> routers = new LinkedList<>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setKey(menu.getId());
            router.setName(menu.getName());
            router.setPath(menu.getPath());
            router.setIcon(menu.getIcon());
            router.setComponent(menu.getComponent());
            // 设置子路由，每个子路由都需要构建成RouterVo
            List<SysMenu> cMenus = menu.getChildren();
            // 子路由不为空 且 属于目录
            if (CollUtil.isNotEmpty(cMenus) && SystemConstants.TYPE_DIR.equals(menu.getType())) {
                router.setChildren(buildMenus(cMenus));
            }
            routers.add(router);
        }
        return routers;
    }

    @Override
    public List<SysMenu> selectMenuListByUserId(SysMenu menu, Long userId) {
        List<SysMenu> menuList = null;
        // 管理员拥有所有菜单信息
        if (LoginHelper.isAdmin(userId)) {
            menuList = baseMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                    .like(StrUtil.isNotBlank(menu.getName()), SysMenu::getName, menu.getName()) // 模糊查询菜单名称
                    .eq(ObjectUtil.isNotNull(menu.getStatus()), SysMenu::getStatus, menu.getStatus()) // 状态查询
                    .orderByAsc(SysMenu::getParentId)
                    .orderByAsc(SysMenu::getMenuSort)
            );
        } else {
            // 非管理员查询菜单信息,根据用户ID查询
            QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
            wrapper.eq("u.id", userId)
                    .like(StrUtil.isNotBlank(menu.getName()), "m.name", menu.getName())
                    .eq(ObjectUtil.isNotNull(menu.getStatus()), "m.status", menu.getStatus())
                    .orderByAsc("m.parent_id")
                    .orderByAsc("m.menu_sort");
            menuList = baseMapper.selectMenuListByUserId(wrapper);
        }
        return menuList;
    }

    @Override
    public SysMenu selectMenuById(Long menuId) {
        return baseMapper.selectById(menuId);
    }

    @Override
    public List<Tree<Long>> buildMenuTreeSelect(List<SysMenu> menus) {
        // 菜单列表为空，则直接返回一个空集合
        if (CollUtil.isEmpty(menus)) {
            return CollUtil.newArrayList();
        }
        // 构建前端所需要下拉树结构
        return TreeBuildUtils.build(menus, (menu, tree) -> {
            tree.setId(menu.getId());
            tree.setParentId(menu.getParentId());
            tree.setName(menu.getName());
            tree.setWeight(menu.getMenuSort());
        });
    }

    @Override
    public String checkMenuNameUnique(SysMenu menu) {
        LambdaQueryWrapper<SysMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(StrUtil.isNotBlank(menu.getName()), SysMenu::getName, menu.getName())
                .ne(ObjectUtil.isNotNull(menu.getId()), SysMenu::getId, menu.getId())
        ;
        boolean exists = baseMapper.exists(lambdaQueryWrapper);
        if (exists) {
            return SystemConstants.NOT_UNIQUE;
        }
        return SystemConstants.UNIQUE;
    }

    @Override
    public int insertMenu(SysMenu menu) {
        return baseMapper.insert(menu);
    }

    @Override
    public int updateMenu(SysMenu menu) {
        return baseMapper.updateById(menu);
    }

    @Override
    public boolean hasChildByMenuId(Long menuId) {
        // 判断 当前菜单ID 是否为 其他菜单的父ID
        return baseMapper.exists(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, menuId));
    }

    @Override
    public boolean checkMenuExistRole(Long menuId) {
        roleMenuMapper.exists(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getMenuId,menuId));
        return false;
    }

    @Override
    public int deleteMenuById(Long menuId) {
        return baseMapper.deleteById(menuId);
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     当前用户拥有的所有菜单信息列表
     * @param parentId 传入的父节点ID
     * @return {@link List }<{@link SysMenu }> 菜单树信息集合
     */
    private List<SysMenu> getChildPerms(List<SysMenu> list, Long parentId) {
        List<SysMenu> returnList = new ArrayList<>();
        for (SysMenu item : list) {
            // 根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (Objects.equals(item.getParentId(), parentId)) {
                // 递归组装，将当前用户所拥有的菜单组装成树的结构
                this.recursionFn(list, item);
                // 将当前组装完的菜单，添加到菜单集合中（默认PID=0，则将下属的全部的一级菜单进行添加）
                returnList.add(item);
            }
        }
        return returnList;
    }

    /**
     * 递归列表（可以解决 无限制下级菜单 问题）
     *
     * @param list  当前用户拥有的所有菜单信息列表
     * @param pItem 父节点
     */
    private void recursionFn(List<SysMenu> list, SysMenu pItem) {
        // 获取子节点列表
        List<SysMenu> childList = this.getChildList(list, pItem);
        // 将该父节点下的所有子节点列表进行封装
        pItem.setChildren(childList);
        // 遍历子节点列表
        for (SysMenu tChild : childList) {
            // 判断该节点下是否存在子节点，递归的出口（没有子节点、树的叶子节点）
            if (this.hasChild(list, tChild)) {
                // 存在，则继续调用自己，递归封装子节点列表
                this.recursionFn(list, tChild);
            }
        }
    }

    /**
     * 获取子节点列表
     *
     * @param list  当前用户拥有的所有菜单信息列表
     * @param pItem 父节点
     * @return {@link List }<{@link SysMenu }> 子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu pItem) {
        // 遍历当前用户拥有的所有菜单信息列表
        // 将 全部菜单信息的ID 与 传入的父节点的PID 进行过滤，保留符合条件菜单节点（ID=PID）
        return StreamUtils.filter(list, item -> item.getParentId().equals(pItem.getId()));
    }

    /**
     * 判断是否有子节点
     *
     * @param list  当前用户拥有的所有菜单信息列表
     * @param pItem 父节点
     * @return boolean 是否存在子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu pItem) {
        // 获取子节点列表，判断大小是否 大于0
        return this.getChildList(list, pItem).size() > 0;
    }
}




