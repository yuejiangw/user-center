package com.yuejiangw.usercenterbackend.service.impl;

import com.yuejiangw.usercenterbackend.common.ErrorCode;
import com.yuejiangw.usercenterbackend.exception.BusinessException;
import com.yuejiangw.usercenterbackend.model.entity.User;
import com.yuejiangw.usercenterbackend.model.entity.mongo.PlanDocument;
import com.yuejiangw.usercenterbackend.service.PlanMongoService;
import com.yuejiangw.usercenterbackend.utils.UserUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * MongoDB 教学计划服务实现类
 */
@Service
@Slf4j
public class PlanMongoServiceImpl implements PlanMongoService {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public String createPlan(PlanDocument planDocument, HttpServletRequest request) {
        final User currentUser = UserUtils.getCurrentUser(request);

        // Set creator and timestamps
        planDocument.setCreatorId(currentUser.getId());
        planDocument.setCreateTime(new Date());
        planDocument.setUpdateTime(new Date());
        planDocument.setIsDelete(0);

        PlanDocument savedPlan = mongoTemplate.save(planDocument);
        log.info("Created plan with ID: {}", savedPlan.getId());
        return savedPlan.getId();
    }

    @Override
    public PlanDocument getPlan(String planId, HttpServletRequest request) {
        final User user = UserUtils.getCurrentUser(request);
        final boolean isAdmin = UserUtils.isAdmin(request);

        Query query = new Query(Criteria.where("_id").is(planId));
        PlanDocument plan = mongoTemplate.findOne(query, PlanDocument.class);

        if (plan == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Plan not found");
        }

        // Check if plan is deleted
        if (plan.getIsDelete() == 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Plan has been deleted");
        }

        // If not admin, can only view plans created by yourself or assigned to you
        if (!isAdmin && !Objects.equals(plan.getCreatorId(), user.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH, "No permission to view this plan");
        }

        return plan;
    }

    @Override
    public List<PlanDocument> searchPlan(Map<String, String> queryParams, HttpServletRequest request) {
        final User user = UserUtils.getCurrentUser(request);
        final boolean isAdmin = UserUtils.isAdmin(request);

        Query query = new Query();
        query.addCriteria(Criteria.where("isDelete").is(0));

        // If not admin, only show plans created by yourself or assigned to you
        if (!isAdmin) {
            query.addCriteria(Criteria.where("creatorId").is(user.getId()));
        }

        // Add search criteria
        queryParams.forEach((key, value) -> {
            switch (key) {
                case "name" -> query.addCriteria(Criteria.where("name").regex(value, "i"));
                case "courseDirection" -> query.addCriteria(Criteria.where("courseDirection").is(value));
                case "subDirection" -> query.addCriteria(Criteria.where("subDirection").is(value));
                case "isPublished" -> query.addCriteria(Criteria.where("isPublished").is(Integer.parseInt(value)));
                case "difficulty" -> query.addCriteria(Criteria.where("difficulty").is(value));
                case "creatorId" -> query.addCriteria(Criteria.where("creatorId").is(Long.parseLong(value)));
            }
        });

        return mongoTemplate.find(query, PlanDocument.class);
    }

    @Override
    public Boolean deletePlan(String planId, HttpServletRequest request) {
        final User user = UserUtils.getCurrentUser(request);
        final boolean isAdmin = UserUtils.isAdmin(request);

        Query query = new Query(Criteria.where("_id").is(planId));
        PlanDocument plan = mongoTemplate.findOne(query, PlanDocument.class);

        if (plan == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Plan not found");
        }

        // If not admin, can only delete plans created by yourself
        if (!isAdmin && !Objects.equals(plan.getCreatorId(), user.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH, "No permission to delete this plan");
        }

        // Soft delete
        Update update = new Update()
                .set("isDelete", 1)
                .set("updateTime", new Date());
        mongoTemplate.updateFirst(query, update, PlanDocument.class);

        log.info("Deleted plan: {}", planId);
        return true;
    }

    @Override
    public Boolean updatePlan(PlanDocument planDocument) {
        Query query = new Query(Criteria.where("_id").is(planDocument.getId()));
        Update update = new Update()
                .set("name", planDocument.getName())
                .set("courseDirection", planDocument.getCourseDirection())
                .set("subDirection", planDocument.getSubDirection())
                .set("courseTarget", planDocument.getCourseTarget())
                .set("courseDetail", planDocument.getCourseDetail())
                .set("updateTime", new Date());

        mongoTemplate.updateFirst(query, update, PlanDocument.class);
        return true;
    }

    @Override
    public List<PlanDocument> getPlansByCreator(Long creatorId, HttpServletRequest request) {
        Query query = new Query(Criteria.where("creatorId").is(creatorId)
                .and("isDelete").is(0));
        return mongoTemplate.find(query, PlanDocument.class);
    }
}