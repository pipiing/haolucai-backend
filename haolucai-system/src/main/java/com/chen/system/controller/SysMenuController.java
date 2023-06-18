package com.chen.system.controller;

import com.chen.service.controller.BaseController;
import com.chen.system.service.ISysMenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "菜单管理接口")
@Validated
@RestController
@RequestMapping("/admin/system/menu")
public class SysMenuController extends BaseController {

    @Autowired
    private ISysMenuService sysMenuService;


}
