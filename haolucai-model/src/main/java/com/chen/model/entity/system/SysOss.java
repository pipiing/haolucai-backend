package com.chen.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chen.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OSS对象存储表
 * @TableName sys_oss
 */
@Data
@Tag(name="SysOss",description = "OSS对象")
@TableName(value ="sys_oss")
@EqualsAndHashCode(callSuper = true)
public class SysOss extends BaseEntity {

    /**
     * 文件名
     */
    @Schema(description = "文件名")
    @TableField(value = "file_name")
    private String fileName;

    /**
     * 文件原始名
     */
    @Schema(description = "文件原始名")
    @TableField(value = "original_name")
    private String originalName;

    /**
     * 文件后缀名
     */
    @Schema(description = "文件后缀名")
    @TableField(value = "file_suffix")
    private String fileSuffix;

    /**
     * URL地址
     */
    @Schema(description = "URL地址")
    @TableField(value = "url")
    private String url;

    /**
     * 服务商（默认minio）
     */
    @Schema(description = "服务商（默认minio）")
    @TableField(value = "service")
    private String service;

    /**
     * 逻辑删除
     */
    @TableLogic
    @Schema(description = "逻辑删除")
    @TableField("is_deleted")
    private Integer isDeleted;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}