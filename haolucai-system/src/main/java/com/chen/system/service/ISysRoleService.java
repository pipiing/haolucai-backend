package com.chen.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.model.entity.system.SysRole;

import java.util.Set;

/**
* @author Pipiing
* @description 针对表【sys_role(角色表)】的数据库操作Service
* @createDate 2023-05-25 09:32:54
*/
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return {@link Set }<{@link String }> 权限字符串集合
     */
    Set<String> selectRolePermissionByUserId(Long userId);
}
