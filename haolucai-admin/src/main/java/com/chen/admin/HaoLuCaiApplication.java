package com.chen.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 后台服务启动程序
 *
 * @author Pipiing
 * @date 2023/05/21 21:33:42
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "com.chen")
public class HaoLuCaiApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(HaoLuCaiApplication.class);
        application.run(args);
        log.info("(♥◠‿◠)ﾉﾞ  HaoLuCai后台服务启动成功   ლ(´ڡ`ლ)ﾞ");
    }

}
