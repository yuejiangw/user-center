package com.yuejiangw.usercenterbackend.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CourseStage implements Serializable {
    private String stageName;

    List<Lesson> lessons;
}
