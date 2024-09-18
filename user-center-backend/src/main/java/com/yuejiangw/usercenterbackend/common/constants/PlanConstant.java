package com.yuejiangw.usercenterbackend.common.constants;

import java.util.List;

public interface PlanConstant {
    String ID = "id";

    String NAME = "name";

    String COURSE_DIR = "courseDirection";

    String SUB_DIR = "subDirection";

    List<String> PLAN_EXACT_MATCH = List.of(ID);

    List<String> PLAN_PATTERN_MATCH = List.of(NAME, COURSE_DIR, SUB_DIR);
}
