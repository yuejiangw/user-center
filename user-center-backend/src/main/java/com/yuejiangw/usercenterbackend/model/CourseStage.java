package com.yuejiangw.usercenterbackend.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class CourseStage implements Serializable {
    @Serial
    private static final long serialVersionUID = -4503874481842441485L;

    private String stageName;

    List<Lesson> lessons;
}
