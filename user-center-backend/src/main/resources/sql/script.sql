create table plan
(
    id              bigint auto_increment comment '计划 id'
        primary key,
    name            varchar(256)                       not null comment '计划名称',
    courseDirection varchar(256)                       not null comment '课程方向',
    subDirection    varchar(256)                       null comment '课程子方向',
    courseTarget    text                               null comment '课程目标',
    courseDetail    json                               null comment '课程安排',
    comment         text                               null comment '备注',
    isDelete        tinyint  default 0                 not null comment '是否删除',
    createTime      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime                           null comment '更新时间'
);

create table user
(
    id           bigint auto_increment
        primary key,
    username     varchar(256)                       null comment '用户昵称',
    userAccount  varchar(256)                       null comment '账号',
    avatarUrl    varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(256)                       not null comment '密码',
    phone        varchar(128)                       null comment '电话号码',
    email        varchar(128)                       null comment '用户邮箱',
    userStatus   int      default 0                 not null comment '用户状态，0 - 正常',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime                           null comment '更新时间',
    isDelete     tinyint  default 0                 null comment '是否删除',
    userRole     int      default 0                 not null comment '用户角色 0 - 普通用户 1 - 管理员'
)
    comment '用户';

create table teach
(
    id          bigint auto_increment
        primary key,
    userId      bigint                             not null comment '用户 id',
    planId      bigint                             not null comment '教学计划 id',
    isPublished tinyint  default 0                 null comment '是否发布，0 - 否，1 - 是',
    isDelete    int                                null comment '是否删除，0-否，1-是',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime                           null comment '更新时间',
    constraint teach_plan_id_fk
        foreign key (planId) references plan (id)
            on update cascade on delete cascade,
    constraint teach_user_id_fk
        foreign key (userId) references user (id)
            on update cascade on delete cascade
);


