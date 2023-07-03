package com.chen.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.model.entity.system.SysOssConfig;

/**
 * @author Pipiing
 * @description 针对表【sys_oss_config(对象存储配置表)】的数据库操作Service
 * @createDate 2023-06-24 19:17:30
 */
public interface ISysOssConfigService extends IService<SysOssConfig> {

    /**
     * 初始化OSS配置
     */
    void init();

}
