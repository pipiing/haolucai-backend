package com.chen.common.utils;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * 日期 工具类
 *
 * @author Pipiing
 * @description
 * @date 2023/06/25 15:06:24
 */
public class DateUtils {

    /**
     * 日期路径（年/月/日 如2023/06/25）
     *
     * @return {@link String } 年/月/日 路径字符串
     */
    public static String datePath() {
        Date now = new Date();
        return DateUtil.format(now, "yyyy/MM/dd");
    }
}
