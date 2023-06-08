package com.chen.system.service;


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
}
