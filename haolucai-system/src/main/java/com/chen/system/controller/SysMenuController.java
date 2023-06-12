package com.chen.system.controller;

import com.chen.system.service.ISysMenuService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "菜单管理接口")
@Validated
@RestController
@RequestMapping("/admin/system/menu")
public class SysMenuController {

    @Autowired
    private ISysMenuService sysMenuService;


}
