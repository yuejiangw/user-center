package com.yuejiangw.usercenterbackend.service;

import com.yuejiangw.usercenterbackend.model.entity.mongo.PlanDocument;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

/**
 * MongoDB 教学计划服务接口
 */
public interface PlanMongoService {

    /**
     * 创建教学计划
     */
    String createPlan(PlanDocument planDocument, HttpServletRequest request);

    /**
     * 根据ID获取教学计划
     */
    PlanDocument getPlan(String planId, HttpServletRequest request);

    /**
     * 搜索教学计划
     */
    List<PlanDocument> searchPlan(Map<String, String> queryParams, HttpServletRequest request);

    /**
     * 删除教学计划
     */
    Boolean deletePlan(String planId, HttpServletRequest request);

    /**
     * 更新教学计划
     */
    Boolean updatePlan(PlanDocument planDocument);

    /**
     * 获取老师创建的所有教学计划
     */
    List<PlanDocument> getPlansByCreator(Long creatorId, HttpServletRequest request);
}