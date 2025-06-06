package com.yuejiangw.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuejiangw.usercenterbackend.common.ErrorCode;
import com.yuejiangw.usercenterbackend.utils.UserUtils;
import com.yuejiangw.usercenterbackend.exception.BusinessException;
import com.yuejiangw.usercenterbackend.model.entity.Plan;
import com.yuejiangw.usercenterbackend.model.entity.User;
import com.yuejiangw.usercenterbackend.service.PlanService;
import com.yuejiangw.usercenterbackend.mapper.PlanMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.yuejiangw.usercenterbackend.common.constants.PlanConstant.PLAN_EXACT_MATCH;
import static com.yuejiangw.usercenterbackend.common.constants.PlanConstant.PLAN_PATTERN_MATCH;

/**
* @author yuejiangwu
* @description 针对表【plan】的数据库操作Service实现
* @createDate 2024-09-15 12:44:36
*/
@Service
@Slf4j
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements PlanService{
    @Resource
    private PlanMapper planMapper;

    @Override
    public long createPlan(Plan plan, HttpServletRequest request) {
        boolean saveResult = this.save(plan);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Plan can not be created");
        }
        return plan.getId();
    }

    @Override
    public Plan getPlan(Long planId, HttpServletRequest request) {
        final User user = UserUtils.getCurrentUser(request);

        final boolean isAdmin = UserUtils.isAdmin(request);
        final Plan plan = planMapper.selectById(planId);

        // make sure planId is valid
        if (plan == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Get plan failed, plan ID doesn't exist");
        }

        // If you are not admin, you can only view the plans created by yourself
        if (!isAdmin && !Objects.equals(plan.getCreatorId(), user.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH, "Get plan failed, you don't have permission to view others' plans");
        }

        return plan;
    }

    @Override
    public List<Plan> searchPlan(Map<String, String> queryParams, HttpServletRequest request) {
        final User user = UserUtils.getCurrentUser(request);
        final QueryWrapper<Plan> queryWrapper = new QueryWrapper<>();

        // 如果不是 admin，则只允许查询自己创建的 plan
        final boolean isAdmin = UserUtils.isAdmin(request);
        if (!isAdmin) {
            queryWrapper.eq("creatorId", user.getId());
        }

        queryParams.forEach((key, value) -> {
            if (PLAN_EXACT_MATCH.contains(key)) {
                queryWrapper.eq(key, value);
            }
            if (PLAN_PATTERN_MATCH.contains(key)) {
                queryWrapper.like(key, value);
            }
        });

        return planMapper.selectList(queryWrapper);
    }

    @Override
    public Boolean deletePlan(Long planId, HttpServletRequest request) {
        final User user = UserUtils.getCurrentUser(request);
        final Plan planToDelete = planMapper.selectById(planId);

        // 如果不是 admin，则只允许删除自己创建的 plan
        final boolean isAdmin = UserUtils.isAdmin(request);
        if (!isAdmin && !Objects.equals(planToDelete.getCreatorId(), user.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH, "Get plan failed, you don't have permission to delete others' plans");
        }

        final int rows = planMapper.deleteById(planId);
        log.info("Delete {} plans", rows);
        return true;
    }

    @Override
    public Boolean updatePlan(Plan plan) {
        return planMapper.updateById(plan) > 0;
    }
}
