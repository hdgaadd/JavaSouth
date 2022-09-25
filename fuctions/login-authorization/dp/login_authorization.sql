/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2022/1/20 21:51:45                           */
/*==============================================================*/


drop table if exists admin;

drop table if exists admin_role_relation;

drop table if exists menu;

drop table if exists permission;

drop table if exists role;

drop table if exists role_menu_relation;

drop table if exists role_permission_relation;

/*==============================================================*/
/* Table: admin                                                 */
/*==============================================================*/
create table admin
(
   id                   bigint(20) NOT NULL AUTO_INCREMENT,
   name                 varchar(64),
   password             varchar(64),
   create_time          datetime,
   login_time           datetime,
   status               int(1),
   primary key (id)
);

/*==============================================================*/
/* Table: admin_role_relation                                   */
/*==============================================================*/
create table admin_role_relation
(
   id                   bigint(20) NOT NULL AUTO_INCREMENT,
   primary key (id)
);

/*==============================================================*/
/* Table: menu                                                  */
/*==============================================================*/
create table menu
(
   id                   bigint(20) NOT NULL AUTO_INCREMENT,
   title                varchar(200),
   parent_id            bigint(20),
   level                bigint(20) comment '²Ëµ¥¼¶Êý',
   create_time          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: permission                                            */
/*==============================================================*/
create table permission
(
   id                   bigint(20) NOT NULL AUTO_INCREMENT,
   name                 varchar(200),
   value                varchar(200),
   url                  varchar(500),
   description          varchar(500),
   create_time          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: role                                                  */
/*==============================================================*/
create table role
(
   id                   bigint(20) NOT NULL AUTO_INCREMENT,
   name                 varchar(64),
   description          varchar(500),
   admin_count          bigint(20),
   status               int(1),
   creat_time           datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: role_menu_relation                                    */
/*==============================================================*/
create table role_menu_relation
(
   id                   bigint(20) NOT NULL AUTO_INCREMENT,
   primary key (id)
);

/*==============================================================*/
/* Table: role_permission_relation                              */
/*==============================================================*/
create table role_permission_relation
(
   id                   bigint(20) NOT NULL AUTO_INCREMENT,
   primary key (id)
);

