package com.chen.system.convert;

import com.chen.model.entity.oss.SysOss;
import com.chen.model.vo.oss.SysOssVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * OSS对象转换接口
 *
 * @author Pipiing
 * @description
 * @date 2023/06/25 15:35:47
 */
@Mapper(componentModel = "spring")
public interface SysOssConvert {

    @Mapping(target = "ossId", source = "id")
    SysOssVo ossToVo(SysOss oss);


}
