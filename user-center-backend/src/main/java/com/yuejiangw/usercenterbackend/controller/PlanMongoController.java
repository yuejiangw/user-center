package com.yuejiangw.usercenterbackend.controller;

import com.yuejiangw.usercenterbackend.common.BaseResponse;
import com.yuejiangw.usercenterbackend.common.ErrorCode;
import com.yuejiangw.usercenterbackend.exception.BusinessException;
import com.yuejiangw.usercenterbackend.model.dto.PlanCreateRequest;
import com.yuejiangw.usercenterbackend.model.dto.PlanUpdateRequest;
import com.yuejiangw.usercenterbackend.model.entity.User;
import com.yuejiangw.usercenterbackend.model.entity.mongo.PlanDocument;
import com.yuejiangw.usercenterbackend.service.PlanMongoService;
import com.yuejiangw.usercenterbackend.utils.ResponseUtils;
import com.yuejiangw.usercenterbackend.utils.UserUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * MongoDB 教学计划控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/plan-mongo")
public class PlanMongoController {

    @Resource
    private PlanMongoService planMongoService;

    @PostMapping("/create")
    public BaseResponse<String> createPlan(@RequestBody final PlanCreateRequest planCreateRequest,
            HttpServletRequest httpServletRequest) {

        final PlanDocument planDocument = PlanDocument.builder()
                .name(planCreateRequest.getName())
                .courseDirection(planCreateRequest.getCourseDirection())
                .subDirection(planCreateRequest.getSubDirection())
                .courseTarget(planCreateRequest.getCourseTarget())
                .courseDetail(planCreateRequest.getCourseDetail())
                .comment(planCreateRequest.getComment())
                .isPublished(0)
                .estimatedDuration(40)
                .build();

        return ResponseUtils.success(planMongoService.createPlan(planDocument, httpServletRequest));
    }

    @GetMapping("/get")
    public BaseResponse<PlanDocument> getPlan(@RequestParam String planId,
            HttpServletRequest httpServletRequest) {
        return ResponseUtils.success(planMongoService.getPlan(planId, httpServletRequest));
    }

    @GetMapping("/search")
    public BaseResponse<List<PlanDocument>> searchPlan(@RequestParam Map<String, String> queryParams,
            final HttpServletRequest request) {
        return ResponseUtils.success(planMongoService.searchPlan(queryParams, request));
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deletePlan(@RequestParam final String planId,
            final HttpServletRequest request) {
        return ResponseUtils.success(planMongoService.deletePlan(planId, request));
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updatePlan(@RequestBody final PlanUpdateRequest planUpdateRequest) {
        if (planUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        final PlanDocument planDocument = PlanDocument.builder()
                .id(planUpdateRequest.getId())
                .name(planUpdateRequest.getName())
                .courseDirection(planUpdateRequest.getCourseDirection())
                .subDirection(planUpdateRequest.getSubDirection())
                .courseTarget(planUpdateRequest.getCourseTarget())
                .courseDetail(planUpdateRequest.getCourseDetail())
                .updateTime(new Date())
                .build();

        return ResponseUtils.success(planMongoService.updatePlan(planDocument));
    }

    @GetMapping("/creator-plans")
    public BaseResponse<List<PlanDocument>> getPlansByCreator(@RequestParam Long creatorId,
            HttpServletRequest request) {
        return ResponseUtils.success(planMongoService.getPlansByCreator(creatorId, request));
    }
}