package com.chen.system.mapper;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.common.constant.SystemConstants;
import com.chen.model.entity.system.SysMenu;
import com.chen.model.entity.system.SysRole;
import com.chen.service.mapper.BaseMapperPlus;

import java.util.List;

/**
 * @author Pipiing
 * @description 针对表【sys_menu(菜单表)】的数据库操作Mapper
 * @createDate 2023-06-01 09:50:55
 * @Entity com.chen.model.entity.system.SysMenu
 */
public interface SysMenuMapper extends BaseMapperPlus<SysMenuMapper,SysMenu,SysMenu> {

    /**
     * 根据用户ID查询菜单权限列表
     *
     * @param userId 用户ID
     * @return {@link List }<{@link SysRole }> 菜单权限列表
     */
    List<String> selectMenuPermsByUserId(Long userId);


    /**
     * 查询全部菜单列表（管理员）
     *
     * @return {@link List }<{@link SysMenu }> 全部菜单树列表
     */
    default List<SysMenu> selectMenuTreeAll() {
        LambdaQueryWrapper<SysMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .in(SysMenu::getType, SystemConstants.TYPE_DIR, SystemConstants.TYPE_MENU) // 属于目录和菜单级别
                .eq(SysMenu::getStatus, SystemConstants.MENU_NORMAL) // 可用菜单
                .orderByAsc(SysMenu::getParentId) // 根据PID升序排序
                .orderByAsc(SysMenu::getMenuSort) // 再根据菜单显示顺序升序排序
        ;
        return this.selectList(lambdaQueryWrapper);
    }

    /**
     * 根据用户ID查询菜单树信息列表
     *
     * @param userId 用户ID
     * @return {@link List }<{@link SysMenu }> 菜单树列表
     */
    List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * 根据用户查询菜单列表
     *
     * @param wrapper 查询条件
     * @return {@link List }<{@link SysMenu }> 菜单列表
     */
    List<SysMenu> selectMenuListByUserId(QueryWrapper<SysMenu> wrapper);
}




