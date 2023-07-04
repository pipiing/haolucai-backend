package com.chen.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common.core.constant.CacheConstants;
import com.chen.common.oss.constant.OssConstant;
import com.chen.common.redis.utils.RedisUtils;
import com.chen.model.entity.system.SysOssConfig;
import com.chen.system.mapper.SysOssConfigMapper;
import com.chen.system.service.ISysOssConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Pipiing
 * @description 针对表【sys_oss_config(对象存储配置表)】的数据库操作Service实现
 * @createDate 2023-06-24 19:17:30
 */
@Service
public class SysOssConfigServiceImpl extends ServiceImpl<SysOssConfigMapper, SysOssConfig>
        implements ISysOssConfigService {

    @Autowired
    private SysOssConfigMapper baseMapper;

    /**
     * 项目启动时，初始化参数到缓存，加载配置类
     */
    @Override
    public void init() {
        List<SysOssConfig> ossConfigList = baseMapper.selectList();
        // 加载OSS初始化配置到Redis缓存中
        for (SysOssConfig ossConfig : ossConfigList) {
            String configKey = ossConfig.getConfigKey();
            // 将默认Oss配置对象进行存储
            if("1".equals(ossConfig.getStatus())){
                RedisUtils.setCacheObject(OssConstant.DEFAULT_CONFIG_KEY, configKey);
            }
            // 使用Redis的Hash存储Oss配置对象
            RedisUtils.setCacheMapValue(CacheConstants.SYS_OSS_CONFIG,configKey, ossConfig);
        }
    }


}




