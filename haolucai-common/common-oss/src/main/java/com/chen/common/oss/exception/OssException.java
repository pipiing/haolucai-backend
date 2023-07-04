package com.chen.common.oss.exception;

/**
 * OSS服务异常类
 *
 * @author Pipiing
 * @description
 * @date 2023/06/25 15:26:43
 */
public class OssException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public OssException(String message) {
        super(message);
    }

}
