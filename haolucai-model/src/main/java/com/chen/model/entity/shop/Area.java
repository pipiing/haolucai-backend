package com.chen.model.entity.shop;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chen.common.mybatis.core.domain.TreeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 地区表
 *
 * @TableName area
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Tag(name = "Area", description = "地区")
@TableName(value = "area")
public class Area extends TreeEntity<Area> {

    /**
     * 地区名称
     */
    @NotBlank(message = "地区名称不能为空")
    @Schema(description = "地区名称")
    @TableField(value = "area_name")
    private String areaName;


    /**
     * 地区级别 0:省、自治区、直辖市 1:地级市、地区、自治州、盟 2:市辖区、县级市、县 3:街道、乡、镇
     */
    @NotBlank(message = "地区级别不能为空")
    @Schema(description = "地区级别 0:省、自治区、直辖市 1:地级市、地区、自治州、盟 2:市辖区、县级市、县 3:街道、乡、镇")
    @TableField(value = "level")
    private String level;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Area{" +
                "id=" + this.getId() + '\'' +
                "areaName='" + areaName + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}