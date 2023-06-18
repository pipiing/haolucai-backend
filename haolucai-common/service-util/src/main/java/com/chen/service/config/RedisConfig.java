package com.chen.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
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
@EnableCaching
public class RedisConfig {

    @Resource
    private ObjectMapper objectMapper;

    @Bean
    public RedissonAutoConfigurationCustomizer redissonCustomizer() {
        log.info("初始化 redis 配置");
        return config -> config.setCodec(new JsonJacksonCodec(objectMapper));
    }


}
