package com.yuejiangw.usercenterbackend.controller;

import com.yuejiangw.usercenterbackend.common.BaseResponse;
import com.yuejiangw.usercenterbackend.common.ErrorCode;
import com.yuejiangw.usercenterbackend.utils.ResponseUtils;
import com.yuejiangw.usercenterbackend.utils.UserUtils;
import com.yuejiangw.usercenterbackend.exception.CustomException;
import com.yuejiangw.usercenterbackend.model.entity.Plan;
import com.yuejiangw.usercenterbackend.model.entity.User;
import com.yuejiangw.usercenterbackend.model.dto.PlanCreateRequest;
import com.yuejiangw.usercenterbackend.model.dto.PlanUpdateRequest;
import com.yuejiangw.usercenterbackend.service.PlanService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/plan")
public class PlanController {

    @Resource
    private PlanService planService;

    @PostMapping("/create")
    public BaseResponse<Long> createPlan(@RequestBody final PlanCreateRequest planCreateRequest, HttpServletRequest httpServletRequest) {
        final User currentUser = UserUtils.getCurrentUser(httpServletRequest);

        final Plan plan = Plan.builder()
                .creatorId(currentUser.getId())
                .name(planCreateRequest.getName())
                .courseDirection(planCreateRequest.getCourseDirection())
                .subDirection(planCreateRequest.getSubDirection())
                .courseTarget(planCreateRequest.getCourseTarget())
                .courseDetail(planCreateRequest.getCourseDetail())
                .comment(planCreateRequest.getComment())
                .build();

        return ResponseUtils.success(planService.createPlan(plan, httpServletRequest));
    }

    @GetMapping("/get")
    public BaseResponse<Plan> getPlan(@RequestParam Long planId, HttpServletRequest httpServletRequest) {
        return ResponseUtils.success(planService.getPlan(planId, httpServletRequest));
    }

    @GetMapping("/search")
    public BaseResponse<List<Plan>> searchPlan(@RequestParam Map<String, String> queryParams, final HttpServletRequest request) {
        return ResponseUtils.success(planService.searchPlan(queryParams, request));
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deletePlan(@RequestParam final long id, final HttpServletRequest request) {
        return ResponseUtils.success(planService.deletePlan(id, request));
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updatePlan(@RequestBody final PlanUpdateRequest planUpdateRequest) {
        if (planUpdateRequest == null) {
            throw new CustomException(ErrorCode.PARAMS_ERROR);
        }

        final Plan plan = Plan.builder()
                .id(planUpdateRequest.getId())
                .name(planUpdateRequest.getName())
                .courseDirection(planUpdateRequest.getCourseDirection())
                .subDirection(planUpdateRequest.getSubDirection())
                .courseTarget(planUpdateRequest.getCourseTarget())
                .courseDetail(planUpdateRequest.getCourseDetail())
                .updateTime(new Date())
                .build();

        return ResponseUtils.success(planService.updatePlan(plan));
    }
}
