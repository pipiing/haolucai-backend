package com.chen.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.model.entity.system.SysRole;

import java.util.List;

/**
* @author Pipiing
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2023-05-25 09:32:54
* @Entity com.chen.model.entity.system.SysRole
*/
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return {@link List }<{@link SysRole }> 角色列表
     */
    List<SysRole> selectRolePermissionByUserId(Long userId);
}




