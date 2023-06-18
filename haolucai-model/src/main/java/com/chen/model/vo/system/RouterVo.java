package com.chen.model.vo.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "唯一标识")
    private Long key;

    /**
     * 路由名字
     */
    @Schema(description = "路由名字")
    private String name;

    /**
     * 图标
     */
    @Schema(description = "图标")
    private String icon;

    /**
     * 路由地址
     */
    @Schema(description = "路由地址")
    private String path;

    /**
     * 组件地址
     */
    @Schema(description = "组件地址")
    private String component;

    /**
     * 子路由
     */
    @Schema(description = "子路由")
    private List<RouterVo> children;

}
