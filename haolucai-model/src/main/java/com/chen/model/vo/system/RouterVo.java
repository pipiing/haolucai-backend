package com.chen.model.vo.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 路由配置信息
 *
 * @author Lion Li
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo {

    /**
     * 路由名字
     */
    @ApiModelProperty("路由名字")
    private String name;

    /**
     * 路由地址
     */
    @ApiModelProperty("路由地址")
    private String path;

    /**
     * 组件地址
     */
    @ApiModelProperty("组件地址")
    private String component;

    /**
     * 路由参数：如 {"id": 1, "name": "ry"}
     */
    @ApiModelProperty("路由参数")
    private String query;

    /**
     * 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
     */
    @ApiModelProperty("是否直接展示子路由")
    private Boolean alwaysShow;

    /**
     * 其他元素
     */
    @ApiModelProperty("其他元素")
    private MetaVo meta;

    /**
     * 子路由
     */
    @ApiModelProperty("子路由")
    private List<RouterVo> children;

}
