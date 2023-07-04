package com.chen.model.dto.system;


import com.chen.common.core.validate.AddGroup;
import com.chen.common.core.validate.EditGroup;
import com.chen.common.mybatis.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 对象存储配置业务DTO对象
 *
 * @author Pipiing
 * @description
 * @date 2023/06/25 09:53:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Tag(name = "SysOssConfigDTO", description = "对象存储配置业务DTO对象")
public class SysOssConfigDTO extends BaseEntity {

    /**
     * 配置key
     */
    @NotBlank(message = "配置key不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 2, max = 100, message = "configKey长度必须介于{min}和{max} 之间")
    private String configKey;

    /**
     * accessKey
     */
    @NotBlank(message = "accessKey不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 2, max = 100, message = "accessKey长度必须介于{min}和{max} 之间")
    private String accessKey;

    /**
     * 秘钥
     */
    @NotBlank(message = "secretKey不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 2, max = 100, message = "secretKey长度必须介于{min}和{max} 之间")
    private String secretKey;

    /**
     * 桶名称
     */
    @NotBlank(message = "桶名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 2, max = 100, message = "bucketName长度必须介于{min}和{max}之间")
    private String bucketName;

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 访问站点
     */
    @NotBlank(message = "访问站点不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 2, max = 100, message = "endpoint长度必须介于{min}和{max}之间")
    private String endpoint;

    /**
     * 自定义域名
     */
    private String domain;

    /**
     * 是否https（Y=是,N=否）
     */
    private String isHttps;

    /**
     * 是否默认（0=否,1=是）
     */
    private String status;

    /**
     * 域
     */
    private String region;

    /**
     * 扩展字段
     */
    private String ext;

    /**
     * 备注
     */
    private String remark;

    /**
     * 桶权限类型(0private 1public 2custom)
     */
    @NotBlank(message = "桶权限类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String accessPolicy;
}
