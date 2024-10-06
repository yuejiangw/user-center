package com.yuejiangw.usercenterbackend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName teach
 */
@TableName(value ="teach")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teach implements Serializable {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 学生 id
     */
    @TableField(value = "studentId")
    private Long studentId;

    /**
     * 老师 id
     */
    @TableField(value = "teacherId")
    private Long teacherId;

    /**
     * 教学计划 id
     */
    @TableField(value = "planId")
    private Long planId;

    /**
     * 是否发布，0 - 否，1 - 是
     */
    @TableField(value = "isPublished")
    private Integer isPublished;

    /**
     * 是否删除，0-否，1-是
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