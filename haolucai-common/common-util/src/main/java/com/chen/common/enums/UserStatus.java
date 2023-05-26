package com.chen.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举类
 *
 * @author Pipiing
 * @date 2023/05/23 21:00:01
 */
@Getter
@AllArgsConstructor
public enum UserStatus {

    DISABLE(0, "停用"),
    OK(1, "正常"),
    ;

    private final Integer code;
    private final String info;

}
