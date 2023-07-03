package com.chen.common.constant;

/**
 * 缓存的key 常量
 *
 * @author Pipiing
 * @date 2022/09/09
 */
public interface CacheConstants {
    /**
     * 登录账户密码错误次数 redis key
     */
    String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * 防重提交 redis key
     */
    String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * OSS配置 redis key
     */
    String SYS_OSS_CONFIG = "sys_oss_config";

    /**
     * 地区数据 redis key（SpringCache缓存Name）
     */
    String AREA = "area";
}
