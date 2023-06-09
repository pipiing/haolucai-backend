package com.chen.model.vo.system;

import com.chen.model.entity.system.SysOss;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * OSS对象存储视图对象 sys_oss
 *
 * @author Pipiing
 * @description
 * @date 2023/06/23 13:03:50
 */
@Data
@Tag(name = "SysOssVo", description = "OSS对象存储视图对象")
@AutoMapper(target = SysOss.class)
public class SysOssVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * OSS对象ID
     */
    @AutoMapping(target = "id")
    @Schema(description = "OSS对象ID")
    private Long ossId;

    /**
     * 文件名
     */
    @Schema(description = "文件名")
    private String fileName;

    /**
     * 文件原始名
     */
    @Schema(description = "文件原始名")
    private String originalName;

    /**
     * 文件后缀名
     */
    @Schema(description = "文件后缀名")
    private String fileSuffix;

    /**
     * URL地址
     */
    @Schema(description = "URL地址")
    private String url;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 上传人
     */
    @Schema(description = "上传人")
    private String createBy;

    /**
     * 服务商
     */
    @Schema(description = "服务商（默认minio）")
    private String service;


}
