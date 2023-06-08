package com.chen.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.util.ObjectUtil;
import com.chen.common.constant.UserConstants;
import com.chen.common.utils.StreamUtils;
import com.chen.model.entity.PageQuery;
import com.chen.model.entity.system.SysRole;
import com.chen.model.entity.system.SysUser;
import com.chen.service.controller.BaseController;
import com.chen.service.helper.LoginHelper;
import com.chen.service.page.TableDataInfo;
import com.chen.service.result.CommonResult;
import com.chen.system.service.ISysMenuService;
import com.chen.system.service.ISysRoleService;
import com.chen.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Pipiing
 * @date 2023/5/22 15:03
 */
@Slf4j
@Api(tags = "用户管理接口")
@Validated
@RestController
@RequestMapping("/admin/system/user")
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService SysUserService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysMenuService sysMenuService;


    /**
     * 获取用户信息条件查询分页列表
     *
     * @param user      用户信息
     * @param pageQuery 分页条件查询
     * @return {@link CommonResult }<{@link TableDataInfo }<{@link SysUser }>> 表格分页数据对象
     */
    @ApiOperation("获取用户列表")
    @SaCheckPermission("system:user:list")
    @GetMapping("/list")
    public CommonResult<TableDataInfo<SysUser>> list(SysUser user, PageQuery pageQuery) {
        TableDataInfo<SysUser> sysUserTableDataInfo = SysUserService.selectPageUserList(user, pageQuery);
        return CommonResult.success(sysUserTableDataInfo);
    }

    /**
     * 根据用户ID获取用户详细信息
     *
     * @param userId 用户ID
     * @return {@link CommonResult }<{@link SysUser }> 用户详细信息
     */
    @ApiOperation("获取用户详细信息")
    @GetMapping("/query/{userId}")
    @SaCheckPermission("system:user:query")
    public CommonResult<Map<String, Object>> queryUserInfo(
            @PathVariable @ApiParam(name = "userId", value = "用户ID", required = true) @NotNull(message = "用户ID不能为空") Long userId
    ) {
        Map<String, Object> ajax = new HashMap<>();
        // 获取全部角色信息
        List<SysRole> roles = sysRoleService.selectRoleAll();
        // 使用Stream流方式，保留当前用户的角色信息
        roles = LoginHelper.isAdmin(userId) ? roles : StreamUtils.filter(roles, r -> !r.isAdmin());
        ajax.put("roles", roles);
        if (ObjectUtil.isNotNull(userId)) {
            SysUser user = SysUserService.selectUserById(userId);
            ajax.put("user", user);
            // 使用Stream流方式，获取RoleList中的全部roleId集合并返回
            ajax.put("roleIds", StreamUtils.toList(user.getRoles(), SysRole::getId));
        }
        return CommonResult.success(ajax);
    }

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     */
    @ApiOperation("新增用户信息")
    @PostMapping("/add")
    @SaCheckPermission("system:user:add")
    public CommonResult<Void> add(@Validated @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(SysUserService.checkUserNameUnique(user))) {
            return CommonResult.error("新增用户[" + user.getUserName() + "]失败，登录账号已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(SysUserService.checkPhoneUnique(user))) {
            return CommonResult.error("新增用户[" + user.getPhone() + "]失败，手机号码已存在");
        }
        // 加密用户登录密码
        user.setPassword(BCrypt.hashpw(user.getPassword()));
        return this.toAjax(SysUserService.insertUser(user));
    }


}
