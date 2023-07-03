package com.chen.admin.runner;

import com.chen.system.service.ISysOssConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化 system 模块对应业务数据
 *
 * @author Pipiing
 * @description
 * @date 2023/06/24 10:32:48
 */
@Slf4j
@Component
public class SystemApplicationRunner implements ApplicationRunner {

    @Autowired
    private ISysOssConfigService sysOssConfigService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 初始化OSS配置成功
        sysOssConfigService.init();
        log.info("初始化OSS配置成功");
    }

}
