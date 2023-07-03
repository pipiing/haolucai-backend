package com.chen.common.utils;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 反射工具类. 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 *
 * @author Pipiing
 * @description
 * @date 2023/06/26 11:26:03
 */
public class ReflectUtils extends ReflectUtil {

    /**
     * 调用Getter方法.
     * 支持多级，如：对象名.对象名.方法
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeGetter(Object obj, String propertyName) {
        Object object = obj;
        // 获取最后一级传入的属性名称（默认"."分割）
        for (String name : StrUtil.split(propertyName, ".")) {
            // 生成Get方法名称（get前缀+属性名称[开头字母大写]）
            String getterMethodName = StrUtil.genGetter(name);
            // 执行对应属性名称的Get方法，获取值
            object = invoke(object, getterMethodName);
        }
        return (E) object;
    }

    /**
     * 调用Setter方法, 仅匹配方法名。
     * 支持多级，如：对象名.对象名.方法
     */
    public static <E> void invokeSetter(Object obj, String propertyName, E value) {
        Object object = obj;
        List<String> nameList = StrUtil.split(propertyName, ".");
        for (int i = 0; i < nameList.size(); i++) {
            if (i < nameList.size() - 1) {
                // 多级前获取对应Get方法
                String getterMethodName = StrUtil.genGetter((nameList.get(i)));
                object = invoke(object, getterMethodName);
            } else {
                // 最后一级，实现赋值Set操作
                String setterMethodName = StrUtil.genSetter((nameList.get(i)));
                Method method = getMethodByName(object.getClass(), setterMethodName);
                invoke(object, method, value);
            }
        }
    }

}
