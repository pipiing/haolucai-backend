package com.chen.system.controller;

import com.chen.model.entity.PageQuery;
import com.chen.model.entity.system.SysUser;
import com.chen.service.controller.BaseController;
import com.chen.service.page.TableDataInfo;
import com.chen.service.result.CommonResult;
import com.chen.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @author Pipiing
 * @date 2023/5/22 15:03
 */
@Slf4j
@Api(tags = "用户管理接口")
@Validated
@RestController
@RequestMapping("/admin/sysUser")
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService SysUserService;


    @ApiOperation("用户查询")
    @GetMapping("/query")
    public CommonResult<SysUser> querySysUser(
            @RequestParam @ApiParam(name = "userId", value = "用户ID", required = true) @NotNull(message = "用户ID不能为空") Long userId
    ) {
        SysUser user = SysUserService.getSysUserByUserId(userId);
        return CommonResult.success(user);
    }

    /**
     * 获取用户信息条件查询分页列表
     *
     * @param user      用户信息
     * @param pageQuery 分页条件查询
     * @return {@link CommonResult }<{@link TableDataInfo }<{@link SysUser }>> 表格分页数据对象
     */
    @ApiOperation("获取用户列表")
    @GetMapping("/list")
    public CommonResult<TableDataInfo<SysUser>> list(SysUser user, PageQuery pageQuery) {
        TableDataInfo<SysUser> sysUserTableDataInfo = SysUserService.selectPageUserList(user, pageQuery);
        return CommonResult.success(sysUserTableDataInfo);
    }


}
