package com.chen.common.security.handler;


import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.hutool.core.util.StrUtil;
import com.chen.common.core.constant.GlobalErrorCodeConstants;
import com.chen.common.core.domain.CommonResult;
import com.chen.common.core.exception.ServiceException;
import com.chen.common.core.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;

/**
 * 全局异常处理器
 *
 * @author Pipiing
 * @date 2022/11/4 16:45
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public CommonResult<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return CommonResult.error(GlobalErrorCodeConstants.SERVICE_ERROR);
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public CommonResult<Void> handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return CommonResult.error(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR);
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResult<Void> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return CommonResult.error(GlobalErrorCodeConstants.METHOD_NOT_ALLOWED);
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public CommonResult<Void> handleServiceException(ServiceException e) {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        return StrUtil.isEmptyIfStr(code) ? CommonResult.error(e.getMessage()) : CommonResult.error(code, e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(value = BindException.class)
    public CommonResult<Void> handleBindException(BindException e){
        log.error(e.getMessage(), e);
        String message = StreamUtils.join(e.getAllErrors(), DefaultMessageSourceResolvable::getDefaultMessage, ", ");
        return CommonResult.error(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResult<Void> constraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        String message = StreamUtils.join(e.getConstraintViolations(), ConstraintViolation::getMessage, ", ");
        return CommonResult.error(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return CommonResult.error(message);
    }

    /**
     * NotLoginException（Sa-Token中的用户未登录异常）
     */
    @ExceptionHandler(NotLoginException.class)
    public CommonResult<Void> handlerNotLoginException(NotLoginException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        // 判断场景值，定制化异常信息
        String message = "";
        if (e.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "未提供token";
        } else if (e.getType().equals(NotLoginException.INVALID_TOKEN)) {
            message = "token无效";
        } else if (e.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            message = "token已过期";
        } else if (e.getType().equals(NotLoginException.BE_REPLACED)) {
            message = "token已被顶下线";
        } else if (e.getType().equals(NotLoginException.KICK_OUT)) {
            message = "token已被踢下线";
        } else {
            message = "当前会话未登录";
        }
        log.error("请求地址'{}',认证失败'{}',无法访问系统资源", requestURI, message);
        // 返回给前端
        return CommonResult.error(GlobalErrorCodeConstants.UNAUTHORIZED);
    }

    /**
     * NotPermissionException（Sa-Token中的缺少权限异常）
     */
    @ExceptionHandler(NotPermissionException.class)
    public CommonResult<Void> handlerNotPermissionException(NotPermissionException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限码校验失败，缺少权限'{}'", requestURI, e.getPermission());
        // 返回给前端
        return CommonResult.error(GlobalErrorCodeConstants.FORBIDDEN);
    }


    /**
     * NotRoleException（Sa-Token中的缺少角色异常）
     */
    @ExceptionHandler(NotRoleException.class)
    public CommonResult<Void> handlerNotNotRoleException(NotRoleException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',角色权限校验失败，缺少角色'{}'", requestURI, e.getRole());
        // 返回给前端
        return CommonResult.error(GlobalErrorCodeConstants.FORBIDDEN);
    }


}
