package com.chen.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.lang.tree.Tree;
import com.chen.common.constant.SystemConstants;
import com.chen.model.entity.system.SysMenu;
import com.chen.service.controller.BaseController;
import com.chen.service.result.CommonResult;
import com.chen.system.service.ISysMenuService;
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
@Tag(name = "菜单管理接口")
@Validated
@RestController
@RequestMapping("/admin/system/menu")
public class SysMenuController extends BaseController {

    @Autowired
    private ISysMenuService sysMenuService;

    /**
     * 获取菜单列表
     *
     * @param menu 菜单信息
     * @return {@link CommonResult }<{@link List }<{@link SysMenu }>> 菜单信息集合
     */
    @Operation(summary = "获取菜单列表")
    @SaCheckPermission("system:menu:list")
    @GetMapping("/list")
    public CommonResult<List<SysMenu>> list(SysMenu menu) {
        return CommonResult.success(sysMenuService.selectMenuListByUserId(menu, this.getUserId()));
    }

    /**
     * 根据菜单ID获取菜单详细信息
     *
     * @param menuId 菜单ID
     * @return {@link CommonResult }<{@link SysMenu }> 菜单信息
     */
    @Operation(summary = "根据菜单ID获取菜单详细信息")
    @SaCheckPermission("system:menu:query")
    @GetMapping("/query/{menuId}")
    public CommonResult<SysMenu> queryMenuInfo(
            @PathVariable @Parameter(name = "menuId", description = "菜单ID", required = true) @NotEmpty(message = "菜单ID不能为空") Long menuId
    ) {
        return CommonResult.success(sysMenuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     *
     * @param menu 菜单信息
     * @return {@link CommonResult }<{@link List }<{@link SysMenu }>> 菜单下拉树结构列表
     */
    @Operation(summary = "获取菜单下拉树列表")
    @GetMapping("/treeSelect")
    @SaCheckPermission("system:menu:treeSelect")
    public CommonResult<List<Tree<Long>>> treeSelect(SysMenu menu) {
        List<SysMenu> menus = sysMenuService.selectMenuListByUserId(menu, getUserId());
        return CommonResult.success(sysMenuService.buildMenuTreeSelect(menus));
    }

    /**
     * 新增菜单信息
     *
     * @param menu 菜单信息
     */
    @Operation(summary = "新增菜单信息")
    @PostMapping("/add")
    @SaCheckPermission("system:menu:add")
    public CommonResult<Void> add(@Validated @RequestBody SysMenu menu) {
        if (SystemConstants.NOT_UNIQUE.equals(sysMenuService.checkMenuNameUnique(menu))) {
            return CommonResult.error("新增菜单[" + menu.getName() + "]失败，菜单名称已存在");
        }
        return toAjax(sysMenuService.insertMenu(menu));
    }

    /**
     * 修改菜单信息
     *
     * @param menu 菜单信息
     */
    @Operation(summary = "修改菜单信息")
    @PutMapping("/edit")
    @SaCheckPermission("system:menu:edit")
    public CommonResult<Void> edit(@Validated @RequestBody SysMenu menu) {
        if (SystemConstants.NOT_UNIQUE.equals(sysMenuService.checkMenuNameUnique(menu))) {
            return CommonResult.error("修改菜单[" + menu.getName() + "]失败，角色名称已存在");
        } else if (menu.getId().equals(menu.getParentId())) {
            return CommonResult.error("修改菜单[" + menu.getName() + "]失败，上级菜单不能选择自己");
        }
        return toAjax(sysMenuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     */
    @Operation(summary = "修改菜单信息")
    @DeleteMapping("/remove/{menuId}")
    @SaCheckPermission("system:menu:remove")
    public CommonResult<Void> remove(
            @PathVariable @Parameter(name = "menuId", description = "菜单ID", required = true) @NotEmpty(message = "菜单ID不能为空") Long menuId
    ) {
        if (sysMenuService.hasChildByMenuId(menuId)) {
            return CommonResult.error("存在子菜单,不允许删除");
        }
        if (sysMenuService.checkMenuExistRole(menuId)) {
            return CommonResult.error("菜单已分配,不允许删除");
        }
        return toAjax(sysMenuService.deleteMenuById(menuId));
    }


}
