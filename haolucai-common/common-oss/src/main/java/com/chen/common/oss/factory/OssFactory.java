package com.chen.common.oss.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.chen.common.core.constant.CacheConstants;
import com.chen.common.core.exception.ServerException;
import com.chen.common.core.utils.BeanCopyUtils;
import com.chen.common.oss.client.OssClient;
import com.chen.common.oss.constant.OssConstant;
import com.chen.common.oss.properties.OssProperties;
import com.chen.common.redis.utils.RedisUtils;
import com.chen.model.entity.system.SysOssConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件上传 工厂（使用工厂设计模式，根据OSS配置类型获取不同类型的实例对象）
 *
 * @author Pipiing
 * @description
 * @date 2023/06/25 10:14:07
 */
@Slf4j
public class OssFactory {

    private static final Map<String, OssClient> CLIENT_CACHE = new ConcurrentHashMap<>();

    /**
     * 获取默认OSS配置实例
     *
     * @return {@link OssClient } Oss客户端对象
     */
    public static OssClient instance() {
        // 从Redis中获取默认类型
        String configKey = RedisUtils.getCacheObject(OssConstant.DEFAULT_CONFIG_KEY);
        if (StrUtil.isBlank(configKey)) {
            throw new ServerException("文件存储服务类型无法找到!");
        }
        return instance(configKey);
    }

    /**
     * 根据OSS配置类型获取实例
     *
     * @param configKey 配置key
     * @return {@link OssClient } Oss客户端对象
     */
    public static OssClient instance(String configKey) {
        // 根据OSS配置类型，从Redis中获取对应OSS配置对象
        SysOssConfig ossConfig = RedisUtils.getCacheMapValue(CacheConstants.SYS_OSS_CONFIG, configKey);
        if (ObjectUtil.isNull(ossConfig)) {
            throw new ServerException("系统异常, '" + configKey + "'配置信息不存在!");
        }
        // 将 OSS配置对象 转换为 OSS对象存储配置属性
        OssProperties ossProperties = BeanCopyUtils.copy(ossConfig, OssProperties.class);
        OssClient client = CLIENT_CACHE.get(configKey);
        // 如果未获取到OssClient对象，则创建一个新OssClient实例对象，并存入ConcurrentHashMap中
        if (client == null) {
            CLIENT_CACHE.put(configKey, new OssClient(configKey, ossProperties));
            log.info("创建OSS实例 key => {}", configKey);
            return CLIENT_CACHE.get(configKey);
        }
        // 配置不相同则重新构建
        if (!client.checkPropertiesSame(ossProperties)) {
            CLIENT_CACHE.put(configKey, new OssClient(configKey, ossProperties));
            log.info("重载OSS实例 key => {}", configKey);
            return CLIENT_CACHE.get(configKey);
        }
        return client;
    }

}
