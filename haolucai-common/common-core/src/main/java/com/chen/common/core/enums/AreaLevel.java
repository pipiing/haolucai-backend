package com.chen.common.core.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 地区等级枚举类（省、市、区县、街道）
 *
 * @author Pipiing
 * @description
 * @date 2023/06/28 16:33:22
 */
@Getter
@AllArgsConstructor
public enum AreaLevel {

    PROVINCE("0", "province"),
    CITY("1", "city"),
    DISTRICT("2", "district"),
    STREET("3", "street");

    private final String code;
    private final String info;

    /**
     * 根据info获取对应地区等级枚举类
     *
     * @param info 信息
     * @return {@link AreaLevel } 地区等级枚举类
     */
    public static AreaLevel getAreaLevel(String info) {
        for (AreaLevel value : values()) {
            if (StrUtil.contains(info, value.getInfo())) {
                return value;
            }
        }
        throw new RuntimeException("没有对应地区等级类型:" + info);
    }


}
