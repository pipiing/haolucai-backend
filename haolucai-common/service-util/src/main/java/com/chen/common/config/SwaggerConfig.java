package com.chen.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * Swagger配置
 *
 * @author Pipiing
 * @date 2023/05/21 21:45:46
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 配置了swagger的Docket的bean实例
     * 该方法返回对象为Docket,并将该对象注入到Bean里面
     *
     * @return {@link Docket}
     */
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo());
    }

    /**
     * 配置swagger的信息等于apiInfo
     *
     * @return {@link ApiInfo}
     */
    private ApiInfo apiInfo() {
        // 作者信息
        Contact contact = new Contact("Pipiing", "https://blog.kuangstudy.com/", "1292379046@qq.com");
        return new ApiInfo("好鲁菜接口文档", "即使再小的帆也能起航", "v1.0",
                "https://blog.kuangstudy.com/", contact, "Apache 2.0", "http:www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>()
        );
    }

}
