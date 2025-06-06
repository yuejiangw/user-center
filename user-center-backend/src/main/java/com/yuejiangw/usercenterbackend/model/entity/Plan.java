package com.yuejiangw.usercenterbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.yuejiangw.usercenterbackend.model.dto.CourseStage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName plan
 */
@TableName(value ="plan", autoResultMap = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Plan implements Serializable {
    /**
     * 计划 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创造者 id
     */
    @TableField(value = "creatorId")
    private Long creatorId;

    /**
     * 计划名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 课程方向
     */
    @TableField(value = "courseDirection")
    private String courseDirection;

    /**
     * 课程子方向
     */
    @TableField(value = "subDirection")
    private String subDirection;

    /**
     * 课程目标
     */
    @TableField(value = "courseTarget")
    private String courseTarget;

    /**
     * 课程安排
     */
    @TableField(typeHandler = JacksonTypeHandler.class, value = "courseDetail")
    private List<CourseStage> courseDetail;

    /**
     * 备注
     */
    @TableField(value = "comment")
    private String comment;

    /**
     * 是否发布，0 - 未发布，1 - 已发布
     */
    @TableField(value = "isPublished")
    private Integer isPublished;

    /**
     * 是否删除
     */
    @TableField(value = "isDelete")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    private Date updateTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}