package com.chen.common.idempotent.config;

import com.chen.common.idempotent.aspect.RepeatSubmitAspect;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConfiguration;

/**
 * 幂等功能配置
 *
 * @author Pipiing
 * @description
 * @date 2023/07/03 18:25:30
 */
@AutoConfigureAfter(RedisConfiguration.class)
public class IdempotentConfig {

    @Bean
    public RepeatSubmitAspect repeatSubmitAspect() {
        return new RepeatSubmitAspect();
    }

}
