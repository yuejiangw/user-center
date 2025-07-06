package com.yuejiangw.usercenterbackend.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class PlanUpdateRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -3567554206991419223L;

    private String id;

    private String name;

    private String courseDirection;

    private List<String> subDirection;

    private String courseTarget;

    private List<CourseStage> courseDetail;

    private String comment;
}
