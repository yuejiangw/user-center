package com.yuejiangw.usercenterbackend.service;

import com.yuejiangw.usercenterbackend.model.domain.Plan;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

/**
* @author yuejiangwu
* @description 针对表【plan】的数据库操作Service
* @createDate 2024-09-15 12:44:36
*/
public interface PlanService extends IService<Plan> {
    void createPlan(final Plan plan, HttpServletRequest request);

    Plan getPlan(final Long planId, HttpServletRequest request);

    List<Plan> searchPlan(Map<String, String> queryParams, HttpServletRequest request);

    Boolean deletePlan(final Long planId, HttpServletRequest request);

    Boolean updatePlan(final Plan plan);
}
