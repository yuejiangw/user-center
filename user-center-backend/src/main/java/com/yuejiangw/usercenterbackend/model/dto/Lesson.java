package com.yuejiangw.usercenterbackend.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Lesson implements Serializable {
    @Serial
    private static final long serialVersionUID = 1287394968777080282L;

    /**
     * 课程内容
     */
    private String content;

    /**
     * 课程时长 - hour
     */
    private Integer courseTimeInHour;

    /**
     * 课程时长 - minute
     */
    private Integer courseTimeInMinute;
}
