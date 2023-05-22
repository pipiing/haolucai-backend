package com.chen.service.config;


import cn.dev33.satoken.interceptor.SaInterceptor;
import com.chen.common.convert.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * web mvc配置
 *
 * @author Pipiing
 * @date 2022/11/30 12:25:28
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    /**
     * 扩展MVC框架的 消息转换器
     *
     * @param converters 转换器
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 1、创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        // 2、设置对象转换器，底层使用JackSon将Java对象转换为Json数据格式
        messageConverter.setObjectMapper(new JacksonObjectMapper());

        // 3、将上面创建的对象转换器，添加到Mvc框架的消息转换器容器中
        // 必须首位使用我们自己创建的对象转换器
        converters.add(0, messageConverter);
    }

    /**
     * 添加拦截器
     * 1、注册 Sa-Token 拦截器，打开注解式鉴权功能
     *
     * @param registry 注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，打开注解式鉴权功能
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }

}