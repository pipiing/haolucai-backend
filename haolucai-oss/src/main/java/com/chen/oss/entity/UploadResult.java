package com.chen.oss.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 上传返回实体类
 *
 * @author Pipiing
 * @description
 * @date 2023/06/23 12:53:12
 */
@Data
@Builder
public class UploadResult {

    /**
     * 文件路径
     */
    private String url;

    /**
     * 文件名
     */
    private String filename;

}
