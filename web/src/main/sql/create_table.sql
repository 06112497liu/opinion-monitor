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
create table bbd_warn_setting
(
   id                   bigint not null,
   name                 varchar(32),
   type                 tinyint comment '1. 事件新增观点预警；2.事件总体热度预警。',
   min                  int,
   max                  int,
   create_by            bigint,
   gmt_create           datetime comment '创建时间',
   modified_by          bigint,
   gmt_modified         datetime comment '修改时间',
   primary key (id)
);

drop table if exists bbd_warn_notifier;
create table bbd_warn_notifier
(
   id                   bigint not null,
   setting_id           bigint,
   notifier             varchar(32),
   email_notify         tinyint(1),
   email                varchar(64),
   sms_notify           tinyint(1),
   phone                varchar(32),
   create_by            bigint,
   gmt_create           datetime comment '创建时间',
   modified_by          bigint,
   gmt_modified         datetime comment '修改时间',
   primary key (id)
);