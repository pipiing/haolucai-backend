package com.chen.model.vo.oss;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;


/**
 * 对象存储配置视图对象 sys_oss_config
 *
 * @author Pipiing
 * @description
 * @date 2023/06/23 15:50:24
 */
@Data
@Tag(name = "SysOssConfigVo", description = "对象存储配置视图对象")
public class SysOssConfigVo {

    /**
     * OSS配置ID
     */
    @Schema(description = "OSS配置ID")
    private Long ossConfigId;

    /**
     * 配置key
     */
    @Schema(description = "配置key")
    private String configKey;

    /**
     * accessKey
     */
    @Schema(description = "accessKey")
    private String accessKey;

    /**
     * 秘钥
     */
    @Schema(description = "秘钥")
    private String secretKey;

    /**
     * 桶名称
     */
    @Schema(description = "桶名称")
    private String bucketName;

    /**
     * 前缀
     */
    @Schema(description = "前缀")
    private String prefix;

    /**
     * 访问站点
     */
    @Schema(description = "访问站点")
    private String endpoint;

    /**
     * 自定义域名
     */
    @Schema(description = "自定义域名")
    private String domain;

    /**
     * 是否https（Y=是,N=否）
     */
    @Schema(description = "是否https（Y=是,N=否）")
    private String isHttps;

    /**
     * 域
     */
    @Schema(description = "域")
    private String region;

    /**
     * 是否默认（0=否,1=是）
     */
    @Schema(description = "是否默认（0=否,1=是）")
    private String status;

    /**
     * 扩展字段
     */
    @Schema(description = "扩展字段")
    private String ext;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 桶权限类型(0:private 1:public 2:custom)
     */
    @Schema(description = "桶权限类型(0:private 1:public 2:custom)")
    private String accessPolicy;

}
