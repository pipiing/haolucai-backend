package com.chen.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础实体类
 *
 * @author Pipiing
 * @date 2023/05/22 10:24:44
 */
@Data
public class BaseEntity implements Serializable {

    /**
     * ID
     */
    @Schema(description="主键ID")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建时间
     */
    @Schema(description="创建时间")
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(description="更新时间")
    @TableField("update_time")
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    @Schema(description="逻辑删除")
    @TableField("is_deleted")
    private Integer isDeleted;

    /**
     * 其他参数
     */
    @TableField(exist = false)
    @Schema(description="其他参数")
    private Map<String, Object> params = new HashMap<>();

}
