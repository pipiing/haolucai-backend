package com.chen.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chen.common.mybatis.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OSS存储配置对象表
 * @TableName sys_oss_config
 */
@Data
@TableName(value ="sys_oss_config")
@Tag(name="SysOssConfig",description = "OSS配置对象")
@EqualsAndHashCode(callSuper = true)
public class SysOssConfig extends BaseEntity {

    /**
     * 配置key
     */
    @Schema(description = "配置key")
    @TableField(value = "config_key")
    private String configKey;

    /**
     * accessKey
     */
    @Schema(description = "accessKey")
    @TableField(value = "access_key")
    private String accessKey;

    /**
     * 秘钥
     */
    @Schema(description = "秘钥")
    @TableField(value = "secret_key")
    private String secretKey;

    /**
     * 桶名称
     */
    @Schema(description = "桶名称")
    @TableField(value = "bucket_name")
    private String bucketName;

    /**
     * 前缀
     */
    @Schema(description = "前缀")
    @TableField(value = "prefix")
    private String prefix;

    /**
     * 访问站点
     */
    @Schema(description = "访问站点")
    @TableField(value = "endpoint")
    private String endpoint;

    /**
     * 自定义域名
     */
    @Schema(description = "自定义域名")
    @TableField(value = "domain")
    private String domain;

    /**
     * 是否https（Y=是,N=否）
     */
    @Schema(description = "是否https（Y=是,N=否）")
    @TableField(value = "is_https")
    private String isHttps;

    /**
     * 域
     */
    @Schema(description = "域")
    @TableField(value = "region")
    private String region;

    /**
     * 桶权限类型(0:private 1:public 2:custom)
     */
    @Schema(description = "桶权限类型(0:private 1:public 2:custom)")
    @TableField(value = "access_policy")
    private String accessPolicy;

    /**
     * 是否默认（0=否,1=是）
     */
    @Schema(description = "是否默认（0=否,1=是）")
    @TableField(value = "status")
    private String status;

    /**
     * 扩展字段
     */
    @Schema(description = "扩展字段")
    @TableField(value = "ext")
    private String ext;

    /**
     * 备注
     */
    @Schema(description = "备注")
    @TableField(value = "remark")
    private String remark;

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