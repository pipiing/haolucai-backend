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
     * 唯一标识
     */
    @ApiModelProperty("唯一标识")
    private Long key;

    /**
     * 路由名字
     */
    @ApiModelProperty("路由名字")
    private String name;

    /**
     * 图标
     */
    @ApiModelProperty("图标")
    private String icon;

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
     * 子路由
     */
    @ApiModelProperty("子路由")
    private List<RouterVo> children;

}
