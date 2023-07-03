package com.chen.system.service;


import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.model.entity.system.SysMenu;
import com.chen.model.vo.system.RouterVo;

import java.util.List;
import java.util.Set;

/**
 * @author Pipiing
 * @description 针对表【sys_menu(菜单表)】的数据库操作Service
 * @createDate 2023-06-01 09:50:55
 */
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 根据用户ID查询菜单权限
     *
     * @param userId 用户ID
     * @return {@link Set }<{@link String }> 菜单权限字符串集合
     */
    Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据用户ID查询菜单树信息
     *
     * @param userId 用户ID
     * @return {@link List }<{@link SysMenu }> 菜单树信息集合
     */
    List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * 建立前端路由菜单
     * route -> index.js 的前端路由
     *
     * @param menus 菜单列表（已构建父子关系的菜单权限集合）
     * @return {@link List }<{@link RouterVo }> 路由列表
     */
    List<RouterVo> buildMenus(List<SysMenu> menus);

    /**
     * 根据用户查询菜单列表
     *
     * @param menu   菜单信息
     * @param userId 用户ID
     * @return {@link List }<{@link SysMenu }> 菜单列表
     */
    List<SysMenu> selectMenuListByUserId(SysMenu menu, Long userId);

    /**
     * 根据菜单ID查询菜单信息
     *
     * @param menuId 菜单ID
     * @return {@link SysMenu } 菜单信息
     */
    SysMenu selectMenuById(Long menuId);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return {@link List }<{@link Tree }<{@link Long }>> 下拉树结构列表
     */
    List<Tree<Long>> buildMenuTreeSelect(List<SysMenu> menus);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return {@link String } 是否唯一结果码
     */
    String checkMenuNameUnique(SysMenu menu);

    /**
     * 新增菜单信息
     *
     * @param menu 菜单信息
     * @return int 插入成功数目
     */
    int insertMenu(SysMenu menu);

    /**
     * 修改菜单信息
     *
     * @param menu 菜单信息
     * @return int 修改成功数目
     */
    int updateMenu(SysMenu menu);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return boolean true:存在 false:不存在
     */
    boolean hasChildByMenuId(Long menuId);

    /**
     * 查询菜单是否被分配给角色
     *
     * @param menuId 菜单ID
     * @return true:存在 false:不存在
     */
    boolean checkMenuExistRole(Long menuId);

    /**
     * 根据菜单ID删除菜单信息
     *
     * @param menuId 菜单ID
     * @return 删除成功数目
     */
    int deleteMenuById(Long menuId);
}
