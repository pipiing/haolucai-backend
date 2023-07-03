package com.chen.system.mapper;

import com.chen.model.entity.system.SysOss;
import com.chen.model.vo.system.SysOssVo;
import com.chen.service.mapper.BaseMapperPlus;
import org.springframework.stereotype.Repository;

/**
* @author Pipiing
* @description 针对表【sys_oss(OSS对象存储表)】的数据库操作Mapper
* @createDate 2023-06-23 15:58:45
* @Entity com.chen.model.SysOss
*/
@Repository
public interface SysOssMapper extends BaseMapperPlus<SysOssMapper,SysOss, SysOssVo> {

}




