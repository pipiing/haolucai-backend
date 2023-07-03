package com.chen.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjectUtil;
import com.chen.common.validate.QueryGroup;
import com.chen.model.dto.system.SysOssDTO;
import com.chen.model.entity.PageQuery;
import com.chen.model.vo.system.SysOssVo;
import com.chen.service.controller.BaseController;
import com.chen.service.page.TableDataInfo;
import com.chen.service.result.CommonResult;
import com.chen.system.service.ISysOssService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Tag(name = "文件上传接口")
@Validated
@RestController
@RequestMapping("/system/oss")
public class SysOssController extends BaseController {

    @Autowired
    private ISysOssService sysOssService;

    /**
     * 获取OSS对象存储信息条件查询分页列表
     *
     * @param dto       OSS对象存储分页查询DTO对象
     * @param pageQuery 分页条件查询
     * @return {@link CommonResult }<{@link TableDataInfo }<{@link SysOssVo }>> OSS存储对象表格分页数据对象
     */
    @Operation(summary = "获取OSS对象存储列表")
    @SaCheckPermission("system:oss:list")
    @GetMapping("/list")
    public CommonResult<TableDataInfo<SysOssVo>> list(@Validated(QueryGroup.class) SysOssDTO dto, PageQuery pageQuery) {
        return CommonResult.success(sysOssService.queryPageList(dto, pageQuery));
    }

    /**
     * 上传OSS对象存储
     *
     * @param file 文件
     * @return {@link CommonResult }<{@link Map }<{@link String }, {@link String }>> 上传文件路径、文件名称
     */
    @Operation(summary = "文件上传")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult<Map<String, String>> upload(
            @RequestPart("file") @Parameter(name = "file", description = "文件", required = true) MultipartFile file
    ) {
        if (ObjectUtil.isNull(file)) {
            return CommonResult.error("上传文件不能为空");
        }
        Map<String, String> map = new HashMap<>(2);
        SysOssVo ossVo = sysOssService.upload(file);
        map.put("url", ossVo.getUrl());
        map.put("fileName", ossVo.getOriginalName());
        map.put("ossId", ossVo.getOssId().toString());
        return CommonResult.success(map);
    }

    /**
     * 下载OSS对象
     *
     * @param ossId    OSS对象ID
     * @param response 响应
     */
    @Operation(summary = "文件下载")
    @GetMapping("/download/{ossId}")
    @SaCheckPermission("system:oss:download")
    public void download(
            @PathVariable @Parameter(name = "ossId", description = "OSS对象ID", required = true) Long ossId,
            HttpServletResponse response
    ) throws IOException {
        sysOssService.download(ossId, response);
    }

    /**
     * 批量删除OSS对象存储
     *
     * @param ossIds OSS对象ID串
     */
    @Operation(summary = "批量删除OSS对象存储")
    @DeleteMapping("/remove/{ossIds}")
    @SaCheckPermission("system:oss:remove")
    public CommonResult<Void> batchRemove(
            @PathVariable  @Parameter(name = "ossIds", description = "OSS对象ID串", required = true) @NotEmpty(message = "OSS对象ID组不能为空") Long[] ossIds
    ){
        return toAjax(sysOssService.deleteByIds(Arrays.asList(ossIds)));
    }




}
