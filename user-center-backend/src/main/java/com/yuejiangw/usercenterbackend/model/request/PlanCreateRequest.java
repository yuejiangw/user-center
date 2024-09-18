package com.yuejiangw.usercenterbackend.model.request;

import com.yuejiangw.usercenterbackend.model.CourseStage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PlanCreateRequest implements Serializable {
    private String name;

    private String courseDirection;

    private String subDirection;

    private String courseTarget;

    private List<CourseStage> courseDetail;

    private String comment;
}