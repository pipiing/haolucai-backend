package com.chen.admin.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.chen.admin.domain.vo.LoginVo;
import com.chen.common.core.domain.CommonResult;
import com.chen.common.core.domain.model.LoginUser;
import com.chen.common.core.domain.model.LoginUserBody;
import com.chen.common.satoken.helper.LoginHelper;
import com.chen.common.web.core.BaseController;
import com.chen.model.entity.system.SysMenu;
import com.chen.model.entity.system.SysUser;
import com.chen.model.vo.system.RouterVo;
import com.chen.system.service.ISysMenuService;
import com.chen.system.service.ISysUserService;
import com.chen.admin.service.AuthService;
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
public class AuthController extends BaseController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysMenuService sysMenuService;


    /**
     * 登录方法
     *
     * @param loginUserBody 登录信息
     */
    @SaIgnore
    @Operation(summary ="用户登陆")
    @PostMapping("/login")
    public CommonResult<LoginVo> login(@Validated @RequestBody LoginUserBody loginUserBody) {
        LoginVo loginVo = new LoginVo();
        // 生成Token令牌
        String token = authService.login(loginUserBody.getUserName(), loginUserBody.getPassword());
        loginVo.setToken(token);
        return CommonResult.success(loginVo);
    }

    /**
     * 退出登录（注销）
     */
    @Operation(summary ="用户注销")
    @PostMapping("/logout")
    public CommonResult<String> logout() {
        authService.logout();
        return CommonResult.success("注销成功");
    }


    /**
     * 获取用户信息
     */
    @Operation(summary ="获取用户信息")
    @GetMapping("/getInfo")
    public CommonResult<Map<String, Object>> getInfo() {
        // Sa-Token 从缓存中获取 登录用户信息
        LoginUser loginUser = LoginHelper.getLoginUser();
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
        Long userId = LoginHelper.getUserId();
        List<SysMenu> menus = sysMenuService.selectMenuTreeByUserId(userId);
        // 构建前端路由所需要的菜单
        return CommonResult.success(sysMenuService.buildMenus(menus));
    }


}
