package com.chen.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.model.entity.system.SysRole;

import java.util.List;
import java.util.Set;

/**
 * @author Pipiing
 * @description 针对表【sys_role(角色表)】的数据库操作Service
 * @createDate 2023-05-25 09:32:54
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 根据用户ID查询角色权限
     *
     * @param userId 用户ID
     * @return {@link Set }<{@link String }> 角色权限字符串集合
     */
    Set<String> selectRolePermissionByUserId(Long userId);

    /**
     * 查询所有角色对象集合
     *
     * @return {@link List }<{@link SysRole }> 角色对象集合
     */
    List<SysRole> selectRoleAll();

    /**
     * 根据用户ID查询角色集合
     *
     * @param userId 用户id
     * @return {@link List }<{@link SysRole }> 角色对象集合
     */
    List<SysRole> selectRolesByUserId(Long userId);
}
