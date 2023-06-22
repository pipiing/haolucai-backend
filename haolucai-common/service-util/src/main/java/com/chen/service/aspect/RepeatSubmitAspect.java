package com.chen.service.aspect;

import cn.dev33.satoken.SaManager;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.chen.common.constant.CacheConstants;
import com.chen.common.utils.RedisUtils;
import com.chen.common.utils.ServletUtils;
import com.chen.service.annotation.RepeatSubmit;
import com.chen.service.exception.ServiceException;
import com.chen.service.exception.enums.GlobalErrorCodeConstants;
import com.chen.service.result.CommonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

/**
 * 防止重复提交(参考美团GTIS防重系统)
 *
 * @author Pipiing
 */
@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class RepeatSubmitAspect {


    /**
     * 缓存Key
     */
    private static final ThreadLocal<String> KEY_CACHE = new ThreadLocal<>();

    /**
     * 处理请求之前
     *
     * @param joinPoint    切点
     * @param repeatSubmit 重复提交注解
     */
    @Before("@annotation(repeatSubmit)")
    public void doBefore(JoinPoint joinPoint, RepeatSubmit repeatSubmit) {
        // 如果注解的间隔时间不为0，则使用注解值
        long interval = 0;
        if (repeatSubmit.interval() > 0) {
            // 将间隔时间 按照注解的时间单位进行转换为毫秒（ms）
            interval = repeatSubmit.timeUnit().toMillis(repeatSubmit.interval());
        }
        // 如果间隔时间小于1s，则抛出异常
        if (interval < 1000) {
            throw new ServiceException("重复提交间隔时间不能小于'1'秒");
        }
        // 获取Request请求对象
        HttpServletRequest request = ServletUtils.getRequest();
        // 拼接请求参数
        String nowParams = this.argsArrayToString(joinPoint.getArgs());
        // 唯一值（当前登录用户Token） SaManager.getConfig().getTokenName()：从配置信息中获取TokenName
        String submitKey = request.getHeader(SaManager.getConfig().getTokenName());
        submitKey = SecureUtil.md5(submitKey + ":" + nowParams);

        // 请求地址（作为存放cache的key值中的一部分）
        String url = request.getRequestURI();
        // 唯一标识（指定Key + url + 请求参数）
        String cacheRepeatKey = CacheConstants.REPEAT_SUBMIT_KEY + url + submitKey;

        // 从Redis中获取对应Key值，如果为null，则说明没有重复提交（设置缓存，以便于下次判断）
        Long key = RedisUtils.getCacheObject(cacheRepeatKey);
        if (key == null) {
            // value：空字符串（因为只需要判断Key是否存在，与内容无关）
            RedisUtils.setCacheObject(cacheRepeatKey, "", Duration.ofMillis(interval));
            // 使用线程隔离缓存当前Key
            KEY_CACHE.set(cacheRepeatKey);
        } else {
            // Key值存在 说明重复提交 抛出异常
            throw new ServiceException(GlobalErrorCodeConstants.REPEATED_REQUESTS);
        }
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint    切点
     * @param repeatSubmit 重复提交注解
     * @param jsonResult   返回json结果
     */
    @AfterReturning(pointcut = "@annotation(repeatSubmit)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, RepeatSubmit repeatSubmit, Object jsonResult) {
        // 判断 方法返回类型是否属于CommonResult类型
        if (jsonResult instanceof CommonResult) {
            try {
                // 转换为CommonResult类型，判断是否执行成功，以便于计算下次是否重复提交
                CommonResult<?> result = (CommonResult<?>) jsonResult;
                if (CommonResult.isSuccess(result.getCode())) {
                    // 成功 则直接返回数据给前端，Redis中仍然存在值
                    return;
                }
                // 失败 则删除Redis中的Key
                RedisUtils.deleteObject(KEY_CACHE.get());
            } finally {
                // 无论成功、失败都将删除线程中存储的Key值
                KEY_CACHE.remove();
            }
        }
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint    切点
     * @param repeatSubmit 重复提交注解
     * @param e            异常
     */
    @AfterThrowing(value = "@annotation(repeatSubmit)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, RepeatSubmit repeatSubmit, Exception e) {
        // 当发生异常时，删除Redis中的Key（保证下次不会重复提交）
        RedisUtils.deleteObject(KEY_CACHE.get());
        // 删除线程中存储的Key
        KEY_CACHE.remove();
    }

    /**
     * 拼接请求参数（组装缓存的Key值）
     *
     * @param paramsArray 请求参数数组
     * @return {@link String }
     */
    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (ArrayUtil.isNotEmpty(paramsArray)) {
            // 遍历请求参数数组，使用StringBuilder拼接字符串
            for (Object obj : paramsArray) {
                if (ObjectUtil.isNotNull(obj)) {
                    try {
                        // 将其他类型的数据转化为Json字符串进行拼接（1L admin admin123）
                        params.append(JSONUtil.toJsonStr(obj)).append(" ");
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        throw new ServiceException("转换请求参数为Json字符串失败");
                    }
                }
            }
        }
        return params.toString().trim();
    }
}
