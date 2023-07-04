package com.chen.common.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 应用程序配置类
 *
 * @author Pipiing
 * @description
 * @date 2023/06/17 14:57:29
 */
@Configuration
@EnableAspectJAutoProxy(exposeProxy = true) // 表示通过aop框架暴露该代理对象，AopContext能够访问
public class ApplicationConfig {
}
