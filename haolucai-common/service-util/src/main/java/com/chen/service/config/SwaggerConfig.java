package com.chen.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置
 *
 * @author Pipiing
 * @date 2023/05/21 21:45:46
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final Contact contact = new Contact("Pipiing", "http://https://github.com/pipiing", "1292379046@qq.com");

    @Bean
    public Docket webApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.chen"))
                // 只显示api路径下的页面
                .paths(PathSelectors.regex("/api/.*"))
                .build();
    }

    @Bean
    public Docket adminApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("adminApi")
                .apiInfo(adminApiInfo())
                .select()
                // 只显示admin路径下的页面
                .paths(PathSelectors.regex("/admin/.*"))
                .build();
    }

    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .title("网站-API文档")
                .description("本文档描述了网站微服务接口定义")
                .version("1.0")
                .contact(contact)
                .build();
    }

    private ApiInfo adminApiInfo() {
        return new ApiInfoBuilder()
                .title("好鲁菜后台管理系统-API文档")
                .description("本文档描述了后台管理系统接口定义")
                .version("1.0")
                .contact(contact)
                .build();
    }


}
