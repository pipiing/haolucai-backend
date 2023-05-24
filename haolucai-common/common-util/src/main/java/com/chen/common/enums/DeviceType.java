package com.chen.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备类型枚举类
 *
 * @author Pipiing
 * @date 2023/05/24 14:16:30
 */
@Getter
@AllArgsConstructor
public enum DeviceType {

    /**
     * pc端
     */
    PC("pc"),


    /**
     * 小程序端
     */
    XCX("xcx");

    private final String device;

}
