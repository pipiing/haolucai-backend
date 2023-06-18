package com.chen.system.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.chen.common.constant.Constants;
import com.chen.model.dto.system.LoginUserDTO;
import com.chen.model.entity.system.LoginUser;
import com.chen.model.entity.system.SysMenu;
import com.chen.model.entity.system.SysUser;
import com.chen.model.vo.system.RouterVo;
import com.chen.service.controller.BaseController;
import com.chen.service.result.CommonResult;
import com.chen.system.service.ISysMenuService;
import com.chen.system.service.ISysUserService;
import com.chen.system.service.SysLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "登录验证接口")
@Validated
@RestController
@RequestMapping("/admin")
public class SysLoginController extends BaseController {

    @Autowired
    private SysLoginService sysLoginService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysMenuService sysMenuService;


    /**
     * 登录方法
     *
     * @param loginUserDTO 登录信息
     */
    @SaIgnore
    @Operation(summary ="用户登陆")
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
    @Operation(summary ="用户注销")
    @PostMapping("/logout")
    public CommonResult<String> logout() {
        sysLoginService.logout();
        return CommonResult.success("注销成功");
    }


    /**
     * 获取用户信息
     */
    @Operation(summary ="获取用户信息")
    @GetMapping("/getInfo")
    public CommonResult<Map<String, Object>> getInfo() {
        // Sa-Token 从缓存中获取 登录用户信息
        LoginUser loginUser = this.getLoginUser();
        // 根据 登录用户ID 获取 数据库中的最新用户信息
        SysUser user = sysUserService.selectUserById(loginUser.getUserId());
        Map<String, Object> ajax = new HashMap<>();
        // 最新用户信息
        ajax.put("user", user);
        // 角色集合
        ajax.put("roles", loginUser.getRolePermission());
        // 权限集合
        ajax.put("permissions", loginUser.getMenuPermission());
        return CommonResult.success(ajax);
    }

    /**
     * 获取路由信息
     */
    @Operation(summary ="获取路由信息")
    @GetMapping("/getRouters")
    public CommonResult<List<RouterVo>> getRouters() {
        // 获取当前登录用户ID
        Long userId = this.getUserId();
        List<SysMenu> menus = sysMenuService.selectMenuTreeByUserId(userId);
        // 构建前端路由所需要的菜单
        return CommonResult.success(sysMenuService.buildMenus(menus));
    }


}
