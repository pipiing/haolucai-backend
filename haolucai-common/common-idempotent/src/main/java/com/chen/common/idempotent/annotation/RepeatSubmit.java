package com.chen.common.idempotent.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 自定义注解防止表单重复提交
 *
 * @author Pipiing
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {

    /**
     * 间隔时间（ms），小于此时间视为重复提交
     */
    int interval() default 5000;

    /**
     * 时间单位 默认：毫秒（ms）
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

}
