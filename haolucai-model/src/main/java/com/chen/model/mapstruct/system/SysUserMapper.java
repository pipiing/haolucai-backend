package com.chen.model.mapstruct.system;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.chen.model.entity.system.SysUser;
import com.chen.model.entity.system.LoginUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * MapStruct Entity、DTO、VO 互相转换
 *
 * @author Pipiing
 * @date 2023/5/22 17:34
 */
@Mapper(componentModel = "spring")
public interface SysUserMapper {

    @Mappings({
            @Mapping(target = "userId", expression = "java(sysUser.getId())"),
            @Mapping(target = "token", expression = "java(saTokenInfo.getTokenValue())")
    })
    LoginUser userToUserVo(SysUser sysUser, SaTokenInfo saTokenInfo);


}
