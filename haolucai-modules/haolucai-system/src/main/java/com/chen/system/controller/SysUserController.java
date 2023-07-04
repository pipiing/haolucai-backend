package com.chen.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.chen.common.core.constant.UserConstants;
import com.chen.common.core.domain.CommonResult;
import com.chen.common.core.utils.StreamUtils;
import com.chen.common.mybatis.core.page.PageQuery;
import com.chen.common.mybatis.core.page.TableDataInfo;
import com.chen.common.satoken.helper.LoginHelper;
import com.chen.common.web.core.BaseController;
import com.chen.model.entity.system.SysRole;
import com.chen.model.entity.system.SysUser;
import com.chen.system.service.ISysRoleService;
import com.chen.system.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "用户管理接口")
@Validated
@RestController
@RequestMapping("/admin/system/user")
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysRoleService sysRoleService;


    /**
     * 获取用户信息条件查询分页列表
     *
     * @param user      用户条件查询参数
     * @param pageQuery 分页条件查询
     * @return {@link CommonResult }<{@link TableDataInfo }<{@link SysUser }>> 用户表格分页数据对象
     */
    @Operation(summary = "获取用户列表")
    @SaCheckPermission("system:user:list")
    @GetMapping("/list")
    public CommonResult<TableDataInfo<SysUser>> list(SysUser user, PageQuery pageQuery) {
        return CommonResult.success(sysUserService.selectPageUserList(user, pageQuery));
    }

    /**
     * 根据用户ID获取用户详细信息
     *
     * @param userId 用户ID
     * @return {@link CommonResult }<{@link SysUser }> 用户详细信息
     */
    @Operation(summary = "根据用户ID获取用户详细信息")
    @GetMapping("/query/{userId}")
    @SaCheckPermission("system:user:query")
    public CommonResult<Map<String, Object>> queryUserInfo(
            @PathVariable @Parameter(name = "userId", description = "用户ID", required = true) @NotNull(message = "用户ID不能为空") Long userId
    ) {
        Map<String, Object> ajax = new HashMap<>();
        // 获取全部角色信息
        List<SysRole> roles = sysRoleService.selectRoleAll();
        // 使用Stream流方式，过滤掉管理员角色
        ajax.put("roles", LoginHelper.isAdmin(userId) ? roles : StreamUtils.filter(roles, r -> !r.isAdmin()));
        if (ObjectUtil.isNotNull(userId)) {
            SysUser user = sysUserService.selectUserById(userId);
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
    @Operation(summary = "新增用户信息")
    @PostMapping("/add")
    @SaCheckPermission("system:user:add")
    public CommonResult<Void> add(@Validated @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(sysUserService.checkUserNameUnique(user))) {
            return CommonResult.error("新增用户[" + user.getUserName() + "]失败，登录账号已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(sysUserService.checkPhoneUnique(user))) {
            return CommonResult.error("新增用户[" + user.getPhone() + "]失败，手机号码已存在");
        }
        // 加密用户登录密码
        user.setPassword(BCrypt.hashpw(user.getPassword()));
        return this.toAjax(sysUserService.insertUser(user));
    }

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     */
    @Operation(summary = "修改用户信息")
    @PutMapping("/edit")
    @SaCheckPermission("system:user:edit")
    public CommonResult<Void> edit(@Validated @RequestBody SysUser user) {
        // 校验用户是否允许操作 -- 管理员用户不能进行操作
        sysUserService.checkUserAllowed(user);
        if (UserConstants.NOT_UNIQUE.equals(sysUserService.checkUserNameUnique(user))) {
            return CommonResult.error("修改用户[" + user.getUserName() + "]失败，登录账号已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(sysUserService.checkPhoneUnique(user))) {
            return CommonResult.error("修改用户[" + user.getPhone() + "]失败，手机号码已存在");
        }
        return toAjax(sysUserService.updateUser(user));
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 用户ID组
     */
    @Operation(summary = "删除用户信息")
    @DeleteMapping("/remove/{userIds}")
    @SaCheckPermission("system:user:remove")
    public CommonResult<Void> remove(
            @PathVariable @Parameter(name = "userIds", description = "用户ID组") Long[] userIds
    ) {
        // 判断是否包含当前用户，当前用户不能被删除
        if (ArrayUtil.contains(userIds, LoginHelper.getUserId())) {
            return CommonResult.error("当前有用户不能删除");
        }
        return toAjax(sysUserService.deleteUserByIds(userIds));
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     */
    @Operation(summary = "重置用户密码")
    @PutMapping("/resetPwd")
    @SaCheckPermission("system:user:resetPwd")
    public CommonResult<Void> resetPwd(@RequestBody SysUser user) {
        sysUserService.checkUserAllowed(user);
        // 加密重置密码
        user.setPassword(BCrypt.hashpw(user.getPassword()));
        return toAjax(sysUserService.resetPwd(user));
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     */
    @Operation(summary = "修改用户状态")
    @PutMapping("/changeStatus")
    @SaCheckPermission("system:user:edit")
    public CommonResult<Void> changeStatus(@RequestBody SysUser user) {
        sysUserService.checkUserAllowed(user);
        return toAjax(sysUserService.updateUserStatus(user));
    }

    /**
     * 根据用户ID获取授权角色集合
     *
     * @param userId 用户ID
     */
    @Operation(summary = "根据用户ID获取授权角色")
    @GetMapping("/query/authRole/{userId}")
    @SaCheckPermission("system:user:query")
    public CommonResult<Map<String, Object>> queryAuthRole(
            @PathVariable @Parameter(name = "userId", description = "用户ID", required = true) @NotNull(message = "用户ID不能为空") Long userId) {
        // 根据用户ID获取用户信息
        SysUser user = sysUserService.selectUserById(userId);
        // 根据用户ID获取授权角色信息
        List<SysRole> roles = sysRoleService.selectRolesByUserId(userId);
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("user", user);
        // 使用Stream流方式，过滤掉管理员角色
        ajax.put("roles", LoginHelper.isAdmin(userId) ? roles : StreamUtils.filter(roles, r -> !r.isAdmin()));
        return CommonResult.success(ajax);
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色ID组
     */
    @Operation(summary = "用户授权角色")
    @PutMapping("/authRole")
    @SaCheckPermission("system:user:edit")
    public CommonResult<Void> insertAuthRole(
            @Parameter(name = "userId", description = "用户ID", required = true) @NotNull(message = "用户ID不能为空") Long userId,
            @Parameter(name = "roleIds", description = "角色ID组") Long[] roleIds
    ) {
        sysUserService.insertUserAuth(userId, roleIds);
        return CommonResult.success();
    }

}
