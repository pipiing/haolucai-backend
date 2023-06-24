package com.chen.service.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.chen.model.entity.BaseEntity;
import com.chen.model.entity.system.LoginUser;
import com.chen.service.exception.ServiceException;
import com.chen.service.helper.LoginHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * MP注入处理器
 * 创建、更新时自动填充注入
 *
 * @author Pipiing
 * @description
 * @date 2023/06/23 22:09:53
 */
@Slf4j
public class CreateAndUpdateMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时自动填充
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            // 判断元数据参数不为Null且属于BaseEntity以及子类
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
                // 强制转换为BaseEntity类型
                BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
                // 根据 baseEntity 的创建时间，获取currentDate，为空则赋值当前时间，不为空则使用原始值
                Date currentDate = ObjectUtil.isNotNull(baseEntity.getCreateTime()) ? baseEntity.getCreateTime() : new Date();
                baseEntity.setCreateTime(currentDate);
                baseEntity.setUpdateTime(currentDate);
                // 根据 baseEntity 的创建人，为空则赋值当前登录用户名称，不为空则使用原始值
                String userName = StrUtil.isNotBlank(baseEntity.getCreateBy()) ? baseEntity.getCreateBy() : this.getLoginUsername();
                baseEntity.setCreateBy(userName);
                baseEntity.setUpdateBy(userName);
            }
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.HTTP_UNAUTHORIZED, "自动注入异常 => " + e.getMessage());
        }
    }

    /**
     * 更新时自动填充
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            // 判断元数据参数不为Null且属于BaseEntity以及子类
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
                // 强制转换为BaseEntity类型
                BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
                // 获取当前时间，填充更新时间
                Date currentDate =  new Date();
                baseEntity.setUpdateTime(currentDate);
                // 获取当前登录用户名，填充更新人
                String userName = this.getLoginUsername();
                if (StrUtil.isNotBlank(userName)){
                    baseEntity.setUpdateBy(userName);
                }
            }
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.HTTP_UNAUTHORIZED, "自动注入异常 => " + e.getMessage());
        }
    }


    /**
     * 获得登录用户名
     *
     * @return {@link String } 当前登录用户名
     */
    private String getLoginUsername() {
        LoginUser loginUser;
        // 如果获取当前登录用户信息时，发生异常则提示日志信息且返回null
        try {
            loginUser = LoginHelper.getLoginUser();
        } catch (Exception e) {
            log.warn("自动注入警告 => 用户未登录");
            return null;
        }
        return ObjectUtil.isNotNull(loginUser) ? loginUser.getUserName() : null;
    }
}
