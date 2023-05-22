package com.chen.system.controller;

import com.chen.model.entity.system.SysUser;
import com.chen.service.result.CommonResult;
import com.chen.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @author Pipiing
 * @date 2023/5/22 15:03
 */
@Slf4j
@Api(tags = "用户管理")
@Validated
@RestController
@RequestMapping("/admin/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("用户查询")
    @GetMapping("/query")
    public CommonResult<SysUser> querySysUser(
            @RequestParam @ApiParam(name = "userId",value = "用户ID",required = true) @NotNull(message = "用户ID不能为空") Long userId
    ) {
        SysUser user = sysUserService.getSysUserByUserId(userId);
        return CommonResult.success(user);
    }

    @ApiOperation("用户登陆")
    @PostMapping("/login")
    public CommonResult login(){
        return CommonResult.success();
    }

}
