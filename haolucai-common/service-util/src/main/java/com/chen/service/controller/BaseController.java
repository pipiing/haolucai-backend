package com.chen.service.controller;


import com.chen.model.entity.system.LoginUser;
import com.chen.service.helper.LoginHelper;
import com.chen.service.result.CommonResult;

/**
 * web层通用数据处理
 *
 * @author Lion Li
 */
public class BaseController {

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected CommonResult<Void> toAjax(int rows) {
        return rows > 0 ? CommonResult.success() : CommonResult.error();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected CommonResult<Void> toAjax(boolean result) {
        return result ? CommonResult.success() : CommonResult.error();
    }


    /**
     * 获取用户缓存信息
     */
    public LoginUser getLoginUser() {
        return LoginHelper.getLoginUser();
    }

    /**
     * 获取登录用户ID
     */
    public Long getUserId() {
        return LoginHelper.getUserId();
    }


    /**
     * 获取登录用户名
     */
    public String getUsername() {
        return LoginHelper.getUsername();
    }
}
