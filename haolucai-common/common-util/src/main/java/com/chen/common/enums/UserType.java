package com.chen.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型枚举类
 *
 * @author Pipiing
 * @date 2023/05/25 08:51:14
 */
@Getter
@AllArgsConstructor
public enum UserType {

    MANAGER(0, "管理员"),
    SELLER(1, "商家"),
    ;

    private final Integer code;
    private final String info;

}
