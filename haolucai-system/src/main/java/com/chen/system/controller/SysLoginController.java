package com.chen.system.controller;

import com.chen.common.constant.Constants;
import com.chen.model.dto.system.LoginUserDTO;
import com.chen.service.controller.BaseController;
import com.chen.service.result.CommonResult;
import com.chen.system.service.SysLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(tags = "登录验证接口")
@Validated
@RestController
@RequestMapping("/admin")
public class SysLoginController extends BaseController {

    @Autowired
    private SysLoginService sysLoginService;


    /**
     * 登录方法
     *
     * @param loginUserDTO 登录信息
     */
    @ApiOperation("用户登陆")
    @PostMapping("/login")
    public CommonResult<Map<String, Object>> login(@Validated @RequestBody LoginUserDTO loginUserDTO) {
        Map<String, Object> ajax = new HashMap<>();
        // 生成Token令牌
        String token = sysLoginService.login(loginUserDTO.getUserName(), loginUserDTO.getPassword());
        ajax.put(Constants.TOKEN, token);
        return CommonResult.success(ajax);
    }

    /**
     * 退出登录（注销）
     */
    @ApiOperation("用户注销")
    @PostMapping("/logout")
    public CommonResult<String> logout() {
        sysLoginService.logout();
        return CommonResult.success("注销成功");
    }


}
