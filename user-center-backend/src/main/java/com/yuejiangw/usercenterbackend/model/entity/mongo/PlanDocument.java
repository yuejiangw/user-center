package com.yuejiangw.usercenterbackend.model.entity.mongo;

import com.yuejiangw.usercenterbackend.model.dto.CourseStage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

/**
 * MongoDB 教学计划文档
 */
@Document(collection = "plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PlanDocument {

    @Id
    private String id;

    /**
     * 创建者ID（关联MySQL用户表）
     */
    @Field("creatorId")
    private Long creatorId;

    /**
     * 计划名称
     */
    @Field("name")
    private String name;

    /**
     * 课程方向
     */
    @Field("courseDirection")
    private String courseDirection;

    /**
     * 课程子方向
     */
    @Field("subDirection")
    private List<String> subDirection;

    /**
     * 课程目标
     */
    @Field("courseTarget")
    private String courseTarget;

    /**
     * 时间线
     */
    @Field("timeline")
    private String timeline;

    /**
     * 项目应用场景
     */
    @Field("scenario")
    private String scenario;

    /**
     * 课程详情
     */
    @Field("courseDetail")
    private List<CourseStage> courseDetail;

    /**
     * 预计学时
     */
    @Field("estimatedDuration")
    private Integer estimatedDuration;

    /**
     * 备注
     */
    @Field("comment")
    private String comment;

    /**
     * 是否发布，0 - 未发布，1 - 已发布
     */
    @Field("isPublished")
    private Integer isPublished;

    /**
     * 是否删除，0 - 未删除，1 - 已删除
     */
    @Field("isDelete")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @Field("createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @Field("updateTime")
    private Date updateTime;
}