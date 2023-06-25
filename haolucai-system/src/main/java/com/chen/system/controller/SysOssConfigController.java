package com.chen.system.controller;

import com.chen.service.controller.BaseController;
import com.chen.system.service.ISysOssConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件存储配置接口
 *
 * @author Pipiing
 * @description
 * @date 2023/06/25 17:04:28
 */
@Slf4j
@Tag(name = "文件存储配置接口")
@Validated
@RestController
@RequestMapping("/system/oss/config")
public class SysOssConfigController extends BaseController {

    @Autowired
    private ISysOssConfigService sysOssConfigService;
}
