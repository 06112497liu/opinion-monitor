DROP TABLE IF EXISTS bbd_user;
CREATE TABLE `bbd_user` (
  `id` BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(64) NOT NULL,
  `password` VARCHAR(32) NOT NULL,
  `gmt_create` DATETIME DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME DEFAULT NULL COMMENT '修改时间',
  UNIQUE(username)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS bbd_account;
CREATE TABLE `bbd_account` (
  `id` BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT(20) NOT NULL,
  `admin` TINYINT(1) NOT NULL DEFAULT 0,
  `name` VARCHAR(64) DEFAULT NULL,
  `phone` VARCHAR(16) DEFAULT NULL,
  `email` VARCHAR(64) DEFAULT NULL,
  `region` VARCHAR(16) DEFAULT NULL,
  `dep_note` VARCHAR(64) DEFAULT NULL,
  `gmt_create` DATETIME DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME DEFAULT NULL COMMENT '修改时间',
  UNIQUE(user_id)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='账户表';

DROP TABLE IF EXISTS bbd_permission;
CREATE TABLE `bbd_permission` (
  `id` BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `code` VARCHAR(64) DEFAULT NULL,
  `name` VARCHAR(64) DEFAULT NULL,
  `gmt_create` DATETIME DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME DEFAULT NULL COMMENT '修改时间',
  UNIQUE(`code`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='权限表';

DROP TABLE IF EXISTS bbd_user_permission;
CREATE TABLE bbd_user_permission(
  id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  permission_id BIGINT NOT NULL,
  gmt_create DATETIME DEFAULT NULL COMMENT '创建时间',
  gmt_modified DATETIME DEFAULT NULL COMMENT '修改时间',
  INDEX idx_user_id(user_id)
) COMMENT='用户权限';

drop table if exists bbd_warn_setting;
/*==============================================================*/
/* Table: bbd_warn_setting                                      */
/*==============================================================*/
create table bbd_warn_setting
(
   id                   bigint not null auto_increment,
   type                 tinyint comment '1. 事件新增观点预警；2.事件总体热度预警；3.舆情预警。',
   name                 varchar(32),
   min                  int,
   max                  int,
   create_by            bigint,
   gmt_create           datetime comment '创建时间',
   modified_by          bigint,
   gmt_modified         datetime comment '修改时间',
   primary key (id)
);

drop table if exists bbd_warn_notifier;
/*==============================================================*/
/* Table: bbd_warn_notifier                                     */
/*==============================================================*/
create table bbd_warn_notifier
(
   id                   bigint not null auto_increment,
   setting_id           bigint,
   notifier             varchar(32),
   email_notify         tinyint(1) DEFAULT '0',
   email                varchar(64),
   sms_notify           tinyint(1) DEFAULT '0',
   phone                varchar(32),
   create_by            bigint,
   gmt_create           datetime comment '创建时间',
   modified_by          bigint,
   gmt_modified         datetime comment '修改时间',
   primary key (id)
);

drop table if exists bbd_opinion;
/*==============================================================*/
/* Table: bbd_opinion                                           */
/*==============================================================*/
create table bbd_opinion
(
   id                   bigint not null auto_increment,
   uuid                 varchar(32),
   title                varchar(255),
   summary             varchar(255),
   content              text,
   source               varchar(32),
   link                 text,
   source_type          tinyint comment '1. 新闻；2.微博；3.微信；4.政务；5.网站；6.论坛；7.其他',
   start_time           datetime,
   hot                  int,
   emotion              tinyint,
   similiar             int,
   words                varchar(128),
   comment_count        int,
   gmt_create           datetime comment '创建时间',
   gmt_modified         datetime comment '修改时间',
   primary key (id)
);

drop table if exists bbd_opinion_event;

/*==============================================================*/
/* Table: bbd_opinion_event                                     */
/*==============================================================*/
create table bbd_opinion_event
(
   id                   bigint not null auto_increment,
   event_name           varchar(64),
   event_group          varchar(16),
   monitor              varchar(16),
   region               varchar(16),
   event_level          varchar(16),
   description          text,
   merchant             varchar(64),
   brand                varchar(32),
   address              varchar(128),
   merchant_tel         varchar(32),
   consumer             varchar(32),
   consumer_tel         varchar(32),
   include_words        text,
   keywords             text,
   exclude_words        text,
   words                text,
   opinion_count        int,
   warn_count           int,
   file_reason          varchar(16) comment '归档事由',
   remark               text comment '备注',
   is_delete            tinyint comment '是否删除，1是，0否',
   create_by            bigint,
   gmt_create           datetime comment '创建时间',
   modified_by          bigint,
   gmt_modified         datetime comment '修改时间',
   primary key (id)
);


drop table if exists bbd_opinion_dictionary;

/*==============================================================*/
/* Table: bbd_opinion_dictionary                                */
/*==============================================================*/
create table bbd_opinion_dictionary
(
   id                   bigint not null auto_increment,
   parent               varchar(16) comment '父代码',
   code                 varchar(16) comment '子代码',
   name                 varchar(64) comment '中文名称',
   gmt_create           datetime comment '创建时间',
   gmt_modified         datetime comment '修改时间',
   primary key (id)
);

alter table bbd_opinion_dictionary comment '事件分组、事发区域、监管主体、事件级别';

drop table if exists bbd_monitor_keywords;

/*==============================================================*/
/* Table: bbd_monitor_keywords                                  */
/*==============================================================*/
create table bbd_monitor_keywords
(
   id                   bigint not null auto_increment,
   value                varchar(32),
   primary key (id)
);

