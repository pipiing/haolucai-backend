package com.chen.service.config;

import com.chen.service.manager.PlusSpringCacheManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * redis配置
 *
 * @author Pipiing
 */
@Slf4j
@Configuration
@EnableCaching // 开启基于注解缓存
public class RedisConfig {

    @Resource
    private ObjectMapper objectMapper;

    @Bean
    public RedissonAutoConfigurationCustomizer redissonCustomizer() {
        log.info("初始化 redis 配置");
        return config -> config.setCodec(new JsonJacksonCodec(objectMapper));
    }

    /**
     * 自定义缓存管理器 整合spring-cache
     */
    @Bean
    public CacheManager cacheManager() {
        return new PlusSpringCacheManager();
    }


}
