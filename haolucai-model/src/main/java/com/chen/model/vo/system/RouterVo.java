package com.chen.model.vo.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 路由配置信息
 *
 * @author Lion Li
 */
@Data
@Tag(name = "RouterVo", description = "路由配置信息")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo implements Serializable {

    private static final long serialVersionUID = 1L;


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
