package com.chen.model.dto.system;


import com.chen.common.mybatis.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OSS对象存储分页查询DTO对象
 *
 * @author Pipiing
 * @description
 * @date 2023/06/25 09:59:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Tag(name = "SysOssDTO", description = "OSS对象存储分页查询DTO对象")
public class SysOssDTO extends BaseEntity {

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 原名
     */
    private String originalName;

    /**
     * 文件后缀名
     */
    private String fileSuffix;

    /**
     * URL地址
     */
    private String url;

    /**
     * 服务商
     */
    private String service;
}
