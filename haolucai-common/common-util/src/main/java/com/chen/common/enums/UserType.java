package com.chen.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 用户类型枚举类
 *
 * @author Pipiing
 * @date 2023/05/25 08:51:14
 */
@Getter
@AllArgsConstructor
public enum UserType {

    MANAGER("0", "管理员"),
    SELLER("1", "商家"),
    ;

    private final String code;
    private final String info;

    public static UserType getUserType(String code) {
        for (UserType value : values()) {
            if (StringUtils.contains(code, value.getCode())) {
                return value;
            }
        }
        throw new RuntimeException("没有对应用户类型:" + code);
    }


}
