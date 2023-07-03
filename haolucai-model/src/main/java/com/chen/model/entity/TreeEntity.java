package com.chen.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.chen.common.validate.AddGroup;
import com.chen.common.validate.EditGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Tree基类
 *
 * @author Pipiing
 * @description
 * @date 2023/06/30 11:11:00
 */
@Data
@Tag(name = "TreeEntity",description = "Tree基类")
@EqualsAndHashCode(callSuper = true)
public class TreeEntity<T> extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父节点名称
     */
    @Schema(description="父节点名称")
    @TableField(exist = false)
    private String parentName;

    /**
     * 父节点ID
     */
    @NotNull(message = "父节点ID不能为空",groups = {AddGroup.class, EditGroup.class})
    @Schema(description="父节点ID")
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 子节点列表
     */
    @Schema(description="子节点列表")
    @TableField(exist = false)
    private List<T> children = new ArrayList<>();

}
