package com.chen.system.mapper;


import com.chen.common.mybatis.core.mapper.BaseMapperPlus;
import com.chen.model.entity.system.SysOssConfig;
import com.chen.model.vo.system.SysOssConfigVo;
import org.springframework.stereotype.Repository;

/**
* @author Pipiing
* @description 针对表【sys_oss_config(对象存储配置表)】的数据库操作Mapper
* @createDate 2023-06-24 19:17:30
* @Entity com.chen.model.SysOssConfig
*/
@Repository
public interface SysOssConfigMapper extends BaseMapperPlus<SysOssConfig, SysOssConfigVo> {

}




