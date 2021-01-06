drop table if exists t_account;
drop table if exists anlz_decider_meta;
drop table if exists anlz_job_meta;
drop table if exists anlz_job_named_param;
drop table if exists anlz_seg_node_def;
drop table if exists anlz_step_link_meta;
drop table if exists anlz_step_meta;
drop table if exists anlz_step_property;

/*==============================================================*/
/* Table: anlz_decider_meta                                     */
/*==============================================================*/
create table anlz_decider_meta
(
    id            bigint        not null comment '主键ID',
    decider_title varchar(64)   not null comment '决策器标题',
    decider_code  varchar(64)   not null comment '决策器编码',
    expression    varchar(1000) not null comment '决策表达式',
    primary key (id)
);

alter table anlz_decider_meta
    comment '决策器元数据';

/*==============================================================*/
/* Table: anlz_job_meta                                         */
/*==============================================================*/
create table anlz_job_meta
(
    id             bigint       not null comment '主键ID',
    job_title      varchar(128) not null comment '作业标题',
    job_code       varchar(64)  not null comment '作业编码',
    tenancy_id     bigint       not null default 0 comment '租户id',
    publish_status int          not null default 0 comment '发布状态：0在建 1已发布',
    update_time    datetime              default CURRENT_TIMESTAMP comment '更新时间',
    primary key (id)
);

alter table anlz_job_meta
    comment '作业元数据';

/*==============================================================*/
/* Table: anlz_job_named_param                                  */
/*==============================================================*/
create table anlz_job_named_param
(
    id            bigint      not null comment '主键ID',
    job_id        bigint      not null comment '作业ID',
    param_name    varchar(64) not null comment '参数名称',
    param_code    varchar(32) not null comment '参数编码',
    data_type     varchar(8)  not null comment '数据类型：string:字符 int:整形 date:日期(yyyy-MM-dd) time:时间(yyyy-MM-dd HH:mm:ss) utc_time:时间(UTC) long:长整形 double:浮点',
    default_value varchar(64) comment '默认值',
    is_require    tinyint comment '是否必须：0否 1是'
);

alter table anlz_job_named_param
    comment '命名参数元数据';

/*==============================================================*/
/* Table: anlz_step_link_meta                                   */
/*==============================================================*/
create table anlz_step_link_meta
(
    id             bigint not null comment '主键ID',
    step_from_id   bigint not null comment '起始步骤ID',
    step_to_id     bigint not null comment '截止步骤ID',
    decider_result varchar(64) comment '决策结果',
    primary key (id)
);

alter table anlz_step_link_meta
    comment '连接元数据';

/*==============================================================*/
/* Table: anlz_step_meta                                        */
/*==============================================================*/
create table anlz_step_meta
(
    id            bigint      not null comment '主键ID',
    step_title    varchar(64) not null comment '步骤标题',
    step_code     varchar(64) not null comment '步骤编码',
    job_id        bigint      not null comment '作业ID',
    seg_node_type varchar(16) not null comment '处理节点类型',
    rectangular_x int         not null,
    rectangular_y int         not null,
    decider_id    bigint comment '决策器ID',
    step_type     int         not null comment '步骤类型：1起始 9结束 2接续',
    primary key (id)
);

alter table anlz_step_meta
    comment '步骤元数据';

/*==============================================================*/
/* Table: anlz_step_property                                    */
/*==============================================================*/
create table anlz_step_property
(
    id      bigint      not null comment '主键ID',
    step_id bigint      not null comment '步骤ID',
    code    varchar(32) not null comment '属性编码',
    seq_no  int default 0 comment '序列',
    content longtext    not null comment '属性内容',
    primary key (id)
);

alter table anlz_step_property
    comment '步骤属性';

create table t_account
(
    id          bigint         not null comment '主键id',
    name        varchar(64)    not null comment '账户名',
    type        int            not null comment '账户类型：1买家 2卖家',
    balance     decimal(10, 2) not null comment '账户余额',
    update_time datetime       not null default CURRENT_TIMESTAMP comment '更新时间',
    primary key (id)
) engine = innodb
  default charset = utf8;

alter table t_account
    comment '账户';

insert into t_account(id, name, type, balance, update_time)
values (5030601, 'rockpile01', 1, 200.00, CURRENT_TIME),
       (5030602, 'rockpile02', 1, 300.00, CURRENT_TIME),
       (5030603, 'rockpile03', 1, 400.00, CURRENT_TIME),
       (5030604, 'rockpile04', 1, 500.00, CURRENT_TIME),
       (5030605, 'rockpile05', 1, 600.00, CURRENT_TIME),
       (5030606, 'rockpile06', 1, 700.00, CURRENT_TIME),
       (5030607, 'rockpile07', 1, 800.00, CURRENT_TIME),
       (5030608, 'rockpile08', 1, 900.00, CURRENT_TIME),
       (5030609, 'rockpile09', 1, 1000.00, CURRENT_TIME),
       (5030610, 'rockpile10', 1, 1100.00, CURRENT_TIME),
       (5030611, 'rockpile11', 1, 1200.00, CURRENT_TIME),
       (5030612, 'rockpile12', 1, 1300.00, CURRENT_TIME),
       (5030613, 'rockpile13', 1, 1400.00, CURRENT_TIME);


