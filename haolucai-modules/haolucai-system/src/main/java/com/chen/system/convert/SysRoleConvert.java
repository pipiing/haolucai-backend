package com.chen.system.convert;


import com.chen.common.core.domain.dto.RoleDTO;
import com.chen.model.entity.system.SysRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * MapStruct Entity、DTO、VO 互相转换
 * 角色转换接口
 *
 * @author Pipiing
 * @description
 * @date 2023/06/05 20:50:53
 */
@Mapper(componentModel = "spring")
public interface SysRoleConvert {

    @Mappings({
            @Mapping(target = "roleId", source = "id")
    })
    RoleDTO roleToDTO(SysRole role);

}