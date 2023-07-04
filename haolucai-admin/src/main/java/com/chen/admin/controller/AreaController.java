package com.chen.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.lang.tree.Tree;
import com.chen.common.core.domain.CommonResult;
import com.chen.common.web.core.BaseController;
import com.chen.model.entity.shop.Area;
import com.chen.model.entity.system.SysMenu;
import com.chen.shop.service.IAreaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Tag(name = "地区管理接口")
@Validated
@RestController
@RequestMapping("/admin/area")
public class AreaController extends BaseController {

    @Autowired
    private IAreaService areaService;

    /**
     * 获取地区列表（条件查询）
     *
     * @param area 地区信息
     * @return {@link CommonResult }<{@link List }<{@link SysMenu }>> 地区信息列表
     */
    @Operation(summary = "获取地区列表")
    @SaCheckPermission("system:area:list")
    @GetMapping("/list")
    public CommonResult<List<Area>> list(Area area) {
        return CommonResult.success(areaService.selectAreaList(area));
    }

    /**
     * 获取地区下拉树列表
     *
     * @param area 地区信息
     * @return {@link CommonResult }<{@link List }<{@link SysMenu }>> 地区下拉树结构列表
     */
    @Operation(summary = "获取地区下拉树列表")
    @GetMapping("/treeSelect")
    @SaCheckPermission("system:area:treeSelect")
    public CommonResult<List<Tree<Long>>> treeSelect(Area area) {
        List<Area> areaList = areaService.selectAreaList(area);
        return CommonResult.success(areaService.buildAreaTreeSelect(areaList));
    }

    /**
     * 获取下级地区列表
     *
     * @param pid pid 父地区ID
     * @return {@link CommonResult }<{@link List }<{@link Area }>> 下级地区列表
     */
    @Operation(summary = "获取下级地区列表")
    @GetMapping("/listByPid/{pid}")
    @SaCheckPermission("system:area:listByPid")
    public CommonResult<List<Area>> listByPid(
            @PathVariable @Parameter(name = "pid", description = "父地区ID") @NotNull(message = "父地区ID不能为空") Long pid
    ) {
        List<Area> areaList = areaService.selectChildAreaListByPid(pid);
        return CommonResult.success(areaList);
    }

    /**
     * 根据地区ID获取地区详细信息
     *
     * @param areaId 地区ID
     * @return {@link CommonResult }<{@link Area }> 地区信息
     */
    @Operation(summary = "获取地区详细信息")
    @SaCheckPermission("system:area:query")
    @GetMapping("/query/{areaId}")
    public CommonResult<Area> queryAreaInfo(
            @PathVariable @Parameter(name = "areaId", description = "地区ID", required = true) @NotNull(message = "地区ID不能为空") Long areaId
    ) {
        return CommonResult.success(areaService.selectAreaById(areaId));
    }

    /**
     * 新增地区信息
     *
     * @param area 地区信息
     */
    @Operation(summary = "新增地区信息")
    @PostMapping("/add")
    @SaCheckPermission("system:area:add")
    public CommonResult<Void> add(@Validated @RequestBody Area area) {
        if (!areaService.checkAreaNameUnique(area)) {
            return CommonResult.error("新增地区[" + area.getAreaName() + "]失败，地区名称已存在");
        }
        return toAjax(areaService.insertArea(area));
    }


}
