package com.chen.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 * @author Pipiing
 * @description
 * @date 2023/06/28 08:06:00
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "com.chen")
public class HaoLuCaiApiApplication
{
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(HaoLuCaiApiApplication.class);
        application.run(args);
        log.info("(♥◠‿◠)ﾉﾞ  HaoLuCai前台服务启动成功   ლ(´ڡ`ლ)ﾞ");
    }

}
