package com.chen.common.oss.constant;

/**
 * oss对象存储常量
 *
 * @author Pipiing
 * @description
 * @date 2023/06/24 19:36:42
 */
public interface OssConstant {

    /**
     * 默认配置KEY
     */
    String DEFAULT_CONFIG_KEY = "sys_oss:default_config";

    /**
     * https 状态
     */
    String IS_HTTPS = "Y";

    /**
     * 云服务商
     */
    String[] CLOUD_SERVICE = new String[] {"aliyun", "qcloud", "qiniu", "obs"};
}
