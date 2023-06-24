package com.chen.model.entity.oss;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chen.model.entity.BaseEntity;
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
    @TableField(value = "file_name")
    private String fileName;

    /**
     * 文件原始名
     */
    @TableField(value = "original_name")
    private String originalName;

    /**
     * 文件后缀名
     */
    @TableField(value = "file_suffix")
    private String fileSuffix;

    /**
     * URL地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 上传人
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 更新人
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 服务商（默认minio）
     */
    @TableField(value = "service")
    private String service;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}