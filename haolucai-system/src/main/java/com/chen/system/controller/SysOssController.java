package com.chen.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjectUtil;
import com.chen.model.vo.oss.SysOssVo;
import com.chen.service.controller.BaseController;
import com.chen.service.result.CommonResult;
import com.chen.system.service.ISysOssService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
     * 文件上传
     *
     * @param file 文件
     * @return {@link CommonResult }<{@link Map }<{@link String }, {@link String }>> 上传文件路径、文件名称
     */
    @Operation(summary = "文件上传")
    @SaCheckPermission("common:oss:upload")
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

}
