package com.chen.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.chen.model.dto.system.LoginUserDTO;
import com.chen.model.entity.system.SysUser;
import com.chen.model.mapstruct.system.SysUserMapper;
import com.chen.model.vo.system.SysUserVo;
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

    @Autowired
    private SysUserMapper sysUserMapper; // MapStruct转换Mapper

    @ApiOperation("用户查询")
    @GetMapping("/query")
    public CommonResult<SysUser> querySysUser(
            @RequestParam @ApiParam(name = "userId", value = "用户ID", required = true) @NotNull(message = "用户ID不能为空") Long userId
    ) {
        SysUser user = sysUserService.getSysUserByUserId(userId);
        return CommonResult.success(user);
    }

    @ApiOperation("用户登陆")
    @PostMapping("/login")
    public CommonResult<SysUserVo> login(@RequestBody LoginUserDTO loginUserDTO) {
        log.info("用户登陆DTO:{}", loginUserDTO);
        // 比较账户、密码是否一致，根据userName、password查询数据库
        SysUser sysUser = sysUserService.getSysUserByUserDTO(loginUserDTO);
        // 根据用户ID，进行登陆（Sa-Token）
        StpUtil.login(sysUser.getId());
        // 获取到该用户的 Token 信息，封装成SysUserVo对象 传递给前端（进行保存）
        log.info("用户登录成功,用户ID:{}",sysUser.getId());
        SysUserVo sysUserVo = sysUserMapper.userToUserVo(sysUser, StpUtil.getTokenInfo());
        return CommonResult.success(sysUserVo);
    }

}
