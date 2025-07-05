package com.yuejiangw.usercenterbackend.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.stereotype.Component;

/**
 * MongoDB 索引初始化器
 */
@Component
@Slf4j
public class MongoIndexInitializer implements CommandLineRunner {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {
        createIndexes();
    }

    /**
     * Create MongoDB indexes
     */
    private void createIndexes() {
        try {
            // Create text index for full-text search
            TextIndexDefinition textIndex = new TextIndexDefinition.TextIndexDefinitionBuilder()
                    .onField("name", 2.0f) // Higher weight for name
                    .onField("courseDirection", 1.5f)
                    .onField("subDirection", 1.5f)
                    .onField("courseTarget", 1.0f)
                    .onField("comment", 1.0f)
                    .build();

            mongoTemplate.indexOps("plans").ensureIndex(textIndex);
            log.info("Created text index for plans collection");

            // Create other indexes
            mongoTemplate.indexOps("plans").ensureIndex(
                    new Index().on("creatorId", org.springframework.data.domain.Sort.Direction.ASC));

            mongoTemplate.indexOps("plans").ensureIndex(
                    new Index().on("isDelete", org.springframework.data.domain.Sort.Direction.ASC));

            mongoTemplate.indexOps("plans").ensureIndex(
                    new Index().on("isPublished", org.springframework.data.domain.Sort.Direction.ASC));

            mongoTemplate.indexOps("plans").ensureIndex(
                    new Index().on("createTime", org.springframework.data.domain.Sort.Direction.DESC));

            log.info("All MongoDB indexes created successfully");

        } catch (Exception e) {
            log.error("Error creating MongoDB indexes: {}", e.getMessage(), e);
        }
    }
}