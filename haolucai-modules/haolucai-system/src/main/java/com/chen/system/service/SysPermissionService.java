package com.chen.system.service;

import com.chen.model.entity.system.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户权限处理
 *
 * @author Pipiing
 * @description
 * @date 2023/06/01 21:35:29
 */
@Slf4j
@Service
public class SysPermissionService {

    @Autowired
    private ISysMenuService sysMenuService;

    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return {@link Set }<{@link String }> 角色权限信息 字符串集合
     */
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            roles.add("admin");
        } else {
            roles.addAll(sysRoleService.selectRolePermissionByUserId(user.getId()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return {@link Set }<{@link String }> 菜单权限信息 字符串集合
     */
    public Set<String> getMenuPermission(SysUser user){
        Set<String> perms = new HashSet<>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            perms.add("*:*:*");
        } else {
            perms.addAll(sysMenuService.selectMenuPermsByUserId(user.getId()));
        }
        return perms;
    }




}
