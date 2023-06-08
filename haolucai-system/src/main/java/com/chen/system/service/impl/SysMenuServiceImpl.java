package com.chen.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common.utils.StreamUtils;
import com.chen.model.entity.system.SysMenu;
import com.chen.model.vo.system.RouterVo;
import com.chen.service.helper.LoginHelper;
import com.chen.system.mapper.SysMenuMapper;
import com.chen.system.service.ISysMenuService;
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
        return getChildPerms(menus, 0);
    }

    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        // TODO 建立前端路由菜单，使用 menu信息 构建 routerVo
        List<RouterVo> routers = new LinkedList<>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setName(menu.getName());
            router.setPath(menu.getPath());
            router.setComponent(menu.getComponent());
            // 只有目录才需要设置是否自动展开
        }
        return null;
    }


    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     当前用户拥有的所有菜单信息列表
     * @param parentId 传入的父节点ID
     * @return {@link List }<{@link SysMenu }> 菜单树信息集合
     */
    private List<SysMenu> getChildPerms(List<SysMenu> list, int parentId) {
        List<SysMenu> returnList = new ArrayList<>();
        for (SysMenu item : list) {
            // 根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (item.getParentId() == parentId) {
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
        return getChildList(list, pItem).size() > 0;
    }


}




