package com.chen.system.mapper;

import com.chen.common.mybatis.core.mapper.BaseMapperPlus;
import com.chen.model.entity.system.SysUserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author Pipiing
* @description 针对表【sys_user_role(用户角色)】的数据库操作Mapper
* @createDate 2023-06-04 09:38:58
* @Entity com.chen.model.entity.system.SysUserRole
*/
@Repository
public interface SysUserRoleMapper extends BaseMapperPlus<SysUserRole, SysUserRole> {

    /**
     * 根据角色ID查询拥有该角色的全部用户ID集合
     *
     * @param roleId 角色ID
     * @return {@link List }<{@link Long }> 拥有该角色的全部用户ID集合
     */
    List<Long> selectUserIdsByRoleId(Long roleId);
}




