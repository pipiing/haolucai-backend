package com.chen.common.enums;

/**
 * 用户状态枚举类
 *
 * @author Pipiing
 * @date 2023/05/23 21:00:01
 */
public enum UserStatus {

    DISABLE(0, "停用"),
    OK(1, "正常"),
    ;

    private final Integer code;
    private final String info;

    UserStatus(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
