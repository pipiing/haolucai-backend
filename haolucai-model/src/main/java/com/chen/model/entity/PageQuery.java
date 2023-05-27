package com.chen.model.entity;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询实体类
 *
 * @author Lion Li
 */

@Data
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分页大小
     */
    @ApiModelProperty("分页大小")
    private Integer pageSize;

    /**
     * 当前页数
     */
    @ApiModelProperty("当前页数")
    private Integer pageNum;

    /**
     * 排序列
     */
    @ApiModelProperty("排序列")
    private String orderByColumn;

    /**
     * 排序的方向desc或者asc
     */
    @ApiModelProperty("排序的方向desc或者asc")
    private String isAsc;

    /**
     * 当前记录起始索引 默认值
     */
    public static final int DEFAULT_PAGE_NUM = 1;

    /**
     * 每页显示记录数 默认值 默认查全部
     */
    public static final int DEFAULT_PAGE_SIZE = Integer.MAX_VALUE;

    /**
     * 构建分页条件
     *
     * @return {@link Page }<{@link T }>
     */
    public <T> Page<T> build() {
        // 进行非空校验，空则设置默认值
        Integer pageNum = ObjectUtil.defaultIfNull(getPageNum(), DEFAULT_PAGE_NUM);
        Integer pageSize = ObjectUtil.defaultIfNull(getPageSize(), DEFAULT_PAGE_SIZE);
        // 逻辑校验（pageNum <= 0 则设置默认值）
        if (pageNum <= 0) {
            pageNum = DEFAULT_PAGE_NUM;
        }
        return new Page<>(pageNum, pageSize);
    }

}
