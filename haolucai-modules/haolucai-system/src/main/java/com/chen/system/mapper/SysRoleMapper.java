package com.chen.system.mapper;

import com.chen.common.mybatis.core.mapper.BaseMapperPlus;
import com.chen.model.entity.system.SysRole;
import com.chen.model.vo.system.SysRoleVo;

import java.util.List;

/**
* @author Pipiing
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2023-05-25 09:32:54
* @Entity com.chen.model.entity.system.SysRole
*/
public interface SysRoleMapper extends BaseMapperPlus<SysRole, SysRoleVo> {

    /**
     * 根据用户ID查询角色对象集合
     *
     * @param userId 用户ID
     * @return {@link List }<{@link SysRole }> 角色对象集合
     */
    List<SysRole> selectRolePermissionByUserId(Long userId);


}




