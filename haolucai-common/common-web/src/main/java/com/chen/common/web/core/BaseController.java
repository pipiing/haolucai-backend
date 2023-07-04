package com.chen.common.web.core;


import cn.hutool.core.util.StrUtil;
import com.chen.common.core.domain.CommonResult;

/**
 * web层通用数据处理
 *
 * @author Pipiing
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
     * 页面跳转
     */
    public String redirect(String url) {
        return StrUtil.format("redirect:{}", url);
    }
}
