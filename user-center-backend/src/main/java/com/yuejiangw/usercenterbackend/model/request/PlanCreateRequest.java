package com.yuejiangw.usercenterbackend.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PlanCreateRequest implements Serializable {
    private String name;

    private String courseDirection;

    private String subDirection;

    private String courseTarget;

    private CourseDetail courseDetail;

    private String comment;
}


@Data
class CourseDetail implements Serializable {
    private List<Stage> stages; // List of stages in the course
}

@Data
class Stage implements Serializable {
    private String stageName; // Name or title of the stage
    private List<Lesson> lessons; // List of lessons in this stage
}

@Data
class Lesson implements Serializable {
    private String content; // Content of the lesson
    private String courseTime; // Time of the lesson (could be formatted as a string, e.g., "2024-09-20 10:00")
}