package com.chen.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.chen.common.core.constant.UserConstants;
import com.chen.common.core.domain.CommonResult;
import com.chen.common.mybatis.core.page.PageQuery;
import com.chen.common.mybatis.core.page.TableDataInfo;
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

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Slf4j
@Tag(name = "角色管理接口")
@Validated
@RestController
@RequestMapping("/admin/system/role")
public class SysRoleController extends BaseController {

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysUserService sysUserService;


    /**
     * 获取角色信息条件查询分页列表
     *
     * @param role      角色条件查询参数
     * @param pageQuery 分页条件查询
     * @return {@link CommonResult }<{@link TableDataInfo }<{@link SysRole }>> 角色表格分页数据对象
     */
    @Operation(summary ="获取角色列表")
    @SaCheckPermission("system:role:list")
    @GetMapping("/list")
    public CommonResult<TableDataInfo<SysRole>> list(SysRole role, PageQuery pageQuery) {
        return CommonResult.success(sysRoleService.selectPageRoleList(role,pageQuery));
    }

    /**
     * 根据角色ID获取角色详细信息
     *
     * @param roleId 角色ID
     * @return {@link CommonResult }<{@link SysRole }> 角色信息
     */
    @Operation(summary ="根据角色ID获取角色详细信息")
    @GetMapping("/query/{roleId}")
    @SaCheckPermission("system:role:query")
    public CommonResult<SysRole> queryRoleInfo(
            @PathVariable @Parameter(name = "roleId", description = "角色ID", required = true) @NotEmpty(message = "角色ID不能为空") Long roleId
    ) {
        return CommonResult.success(sysRoleService.selectRoleById(roleId));
    }

    /**
     * 新增角色信息
     *
     * @param role 角色信息
     */
    @Operation(summary ="新增角色信息")
    @PostMapping("/add")
    @SaCheckPermission("system:role:add")
    public CommonResult<Void> add(@Validated @RequestBody SysRole role) {
        if (UserConstants.NOT_UNIQUE.equals(sysRoleService.checkRoleNameUnique(role))) {
            return CommonResult.error("新增角色[" + role.getRoleName() + "]失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(sysRoleService.checkRoleKeyUnique(role))) {
            return CommonResult.error("新增角色[" + role.getRoleKey() + "]失败，角色权限已存在");
        }
        return toAjax(sysRoleService.insertRole(role));
    }

    /**
     * 修改角色信息
     *
     * @param role 角色信息
     */
    @Operation(summary ="修改角色信息")
    @PutMapping("/edit")
    @SaCheckPermission("system:role:edit")
    public CommonResult<Void> edit(@Validated @RequestBody SysRole role) {
        // 校验角色是否允许操作 -- 管理员角色不能进行操作
        sysRoleService.checkRoleAllowed(role);
        if (UserConstants.NOT_UNIQUE.equals(sysRoleService.checkRoleNameUnique(role))) {
            return CommonResult.error("修改角色[" + role.getRoleName() + "]失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(sysRoleService.checkRoleKeyUnique(role))) {
            return CommonResult.error("修改角色[" + role.getRoleKey() + "]失败，角色权限已存在");
        }
        // 如果修改角色信息成功，则需要强制注销在线且拥有该角色权限的用户
        if (sysRoleService.updateRole(role) > 0){
            sysRoleService.cleanOnlineUserByRole(role.getId());
            return CommonResult.success();
        }
        return CommonResult.error("修改角色[" + role.getRoleName() + "]失败");
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 角色ID组
     */
    @Operation(summary ="删除角色信息")
    @DeleteMapping("/remove/{roleIds}")
    @SaCheckPermission("system:role:remove")
    public CommonResult<Void> remove(
            @PathVariable @Parameter(name = "roleIds", description = "角色ID组") Long[] roleIds
    ){
        return toAjax(sysRoleService.deleteRoleByIds(roleIds));
    }

    /**
     * 获取角色选择框列表
     *
     * @return {@link CommonResult }<{@link List }<{@link SysRole }>> 所有角色对象集合
     */
    @Operation(summary ="获取角色选择框列表")
    @GetMapping("/query/optionSelect")
    @SaCheckPermission("system:role:query")
    public CommonResult<List<SysRole>> queryOptionSelect() {
        return CommonResult.success(sysRoleService.selectRoleAll());
    }

    /**
     * 查询已分配用户角色列表
     *
     * @param user      用户条件查询参数
     * @param pageQuery 分页条件查询
     * @return {@link CommonResult }<{@link TableDataInfo }<{@link SysUser }>> 用户信息集合信息
     */
    @Operation(summary ="查询已分配用户角色列表")
    @SaCheckPermission("system:role:list")
    @GetMapping("/authUser/assignedList")
    public CommonResult<TableDataInfo<SysUser>> assignedList(SysUser user, PageQuery pageQuery) {
        return CommonResult.success(sysUserService.selectAssignedList(user, pageQuery));
    }

    /**
     * 查询未分配用户角色列表
     *
     * @param user      用户条件查询参数
     * @param pageQuery 分页条件查询
     * @return {@link CommonResult }<{@link TableDataInfo }<{@link SysUser }>> 用户信息集合信息
     */
    @Operation(summary ="查询未分配用户角色列表")
    @SaCheckPermission("system:role:list")
    @GetMapping("/authUser/unAssignedList")
    public CommonResult<TableDataInfo<SysUser>> unassignedList(SysUser user, PageQuery pageQuery) {
        return  CommonResult.success(sysUserService.selectUnAssignedList(user, pageQuery));
    }

    /**
     * 批量取消授权用户
     *
     * @param roleId  角色ID
     * @param userIds 用户ID组
     */
    @Operation(summary ="批量取消授权用户")
    @SaCheckPermission("system:role:edit")
    @PutMapping("/authUser/cancelAll")
    public CommonResult<Void> cancelAuthUserAll(
            @Parameter(name = "roleId", description = "角色ID") @NotEmpty(message = "角色ID不能为空") Long roleId,
            @Parameter(name = "userIds", description = "用户ID组") Long[] userIds
    ){
        return toAjax(sysRoleService.deleteAuthUsers(roleId,userIds));
    }

    /**
     * 批量选择用户授权
     *
     * @param roleId  角色ID
     * @param userIds 用户ID组
     */
    @Operation(summary ="批量选择用户授权")
    @SaCheckPermission("system:role:edit")
    @PostMapping("/authUser/selectAll")
    public CommonResult<Void> selectAuthUserAll(
            @Parameter(name = "roleId", description = "角色ID") @NotEmpty(message = "角色ID不能为空") Long roleId,
            @Parameter(name = "userIds", description = "用户ID组") Long[] userIds
    ){
        return toAjax(sysRoleService.insertAuthUsers(roleId, userIds));
    }
}
