drop table if exists TSYS_SYSTEM_ACCESS_LOG;
drop table if exists TSYS_ACCOUNT_SHORTCUTS;
drop table if exists TSYS_MODULE_URL;
drop table if exists TSYS_ROLE_POPEDOM;
drop table if exists TSYS_ACCOUNT_ROLE;
drop table if exists TSYS_MODULE_HELP ;
drop table if exists TSYS_MODULE;
drop table if exists TSYS_MENU;
drop table if exists TSYS_ROLE;
drop table if exists TSYS_ACCOUNT_USE_HISTORY;
drop table if exists TSYS_ACCOUNT;

create table TSYS_ACCOUNT
(
   accountId            varchar(36) not null,
   loginName            varchar(30) not null,
   realName             varchar(40) not null,
   password             varchar(32) not null,
   email                varchar(120),
   tel                  varchar(15),
   state                char(1) not null default '0',
   uiStyle              char(1) not null default '0',
   primary key (accountId)
);

create unique index INU_TSYS_A_LN on TSYS_ACCOUNT
(
   loginName
);

create table TSYS_ACCOUNT_ROLE
(
   guid                 varchar(36) not null,
   accountId            varchar(36) not null,
   roleId               varchar(36) not null,
   primary key (guid)
);

create index TSYS_A_R_Index_1 on TSYS_ACCOUNT_ROLE
(
   accountId
);

create index TSYS_A_R_Index_2 on TSYS_ACCOUNT_ROLE
(
   roleId
);

create table TSYS_ACCOUNT_SHORTCUTS
(
   guid                 varchar(36) not null,
   menuId               varchar(36) not null,
   accountId            varchar(36) not null,
   orderNum             int not null default 0,
   primary key (guid)
);

create unique index TSYS_A_Shortcuts_Index_1 on TSYS_ACCOUNT_SHORTCUTS
(
   menuId
);

create index TSYS_A_Shortcuts_Index_2 on TSYS_ACCOUNT_SHORTCUTS
(
   accountId
);

create table TSYS_ACCOUNT_USE_HISTORY
(
   guid                 varchar(36) not null,
   accountId            varchar(36) not null,
   menuId               varchar(36) not null,
   amount               int not null default 0,
   lastAccessDateTime   datetime,
   primary key (guid)
);

create index TSYS_A_U_H_Index_1 on TSYS_ACCOUNT_USE_HISTORY
(
   accountId
);

create index TSYS_A_U_H_Index_2 on TSYS_ACCOUNT_USE_HISTORY
(
   menuId
);

create index TSYS_A_U_H_Index_3 on TSYS_ACCOUNT_USE_HISTORY
(
   lastAccessDateTime
);

create table TSYS_MENU
(
   menuId               varchar(36) not null,
   parentId             varchar(36) default '0',
   menuName             varchar(20) not null,
   hint                 varchar(60),
   entryURL             varchar(512),
   orderNum             int not null default 0,
   icon                 varchar(36),
   primary key (menuId)
);

alter table TSYS_MENU
   add unique AK_parentId_menuName (parentId, menuName);

create index IN_TSYS_Menu_menuName on TSYS_MENU
(
   menuName
);

create table TSYS_MODULE
(
   moduleId             varchar(36) not null,
   menuId               varchar(36) not null,
   moduleName           varchar(120) not null,
   intro                varchar(512),
   primary key (moduleId)
);

create unique index INU_TSYS_ModuleModuleName on TSYS_MODULE
(
   moduleName
);

create index TSYS_Module_Index_2 on TSYS_MODULE
(
   menuId
);

create table TSYS_MODULE_HELP
(
   helpId               varchar(36) not null,
   helpTcpic            varchar(260) not null,
   helpContent          varchar(1200) not null,
   operateTime          datetime not null,
   primary key (helpId)
);

create table TSYS_MODULE_URL
(
   guid                 varchar(36) not null,
   moduleId             varchar(36) not null,
   url                  varchar(256) not null,
   method               varchar(10) not null,
   primary key (guid)
);

create index INU_TSYS_Module_URL on TSYS_MODULE_URL
(
   moduleId
);

create table TSYS_ROLE
(
   roleId               varchar(36) not null,
   roleName             varchar(60) not null,
   primary key (roleId)
);

create unique index INU_TSYS_Role_RoleName on TSYS_ROLE
(
   roleName
);

create table TSYS_ROLE_POPEDOM
(
   guid                 varchar(36) not null,
   roleId               varchar(36) not null,
   moduleId             varchar(36) not null,
   isShortcut           char(1) not null default '0',
   primary key (guid)
);

create index TSYS_Role_P_Index_1 on TSYS_ROLE_POPEDOM
(
   roleId
);

create index TSYS_Role_P_Index_2 on TSYS_ROLE_POPEDOM
(
   moduleId
);

create table TSYS_SYSTEM_ACCESS_LOG
(
   guid                 varchar(36) not null,
   accountId            varchar(36) not null,
   loginName            varchar(30) not null,
   realName             varchar(40) not null,
   accessIP             varchar(36) not null,
   accessDateTime       datetime not null,
   browser              varchar(12),
   browserDetail        varchar(360),
   command              varchar(2048),
   primary key (guid, accessDateTime)
);

create index TSYS_S_A_L_Index_1 on TSYS_SYSTEM_ACCESS_LOG
(
   accountId
);

create index TSYS_S_A_L_Index_2 on TSYS_SYSTEM_ACCESS_LOG
(
   loginName
);

create index TSYS_S_A_L_Index_3 on TSYS_SYSTEM_ACCESS_LOG
(
   accessDateTime
);

alter table TSYS_ACCOUNT_ROLE add constraint FK_T_AR_1 foreign key (accountId)
      references TSYS_ACCOUNT (accountId) on delete restrict on update restrict;

alter table TSYS_ACCOUNT_ROLE add constraint FK_T_AR_2 foreign key (roleId)
      references TSYS_ROLE (roleId) on delete restrict on update restrict;

alter table TSYS_ACCOUNT_SHORTCUTS add constraint FK_T_AS_2 foreign key (menuId)
      references TSYS_MENU (menuId);

alter table TSYS_ACCOUNT_USE_HISTORY add constraint FK_T_AUH_1 foreign key (accountId)
      references TSYS_ACCOUNT (accountId);

alter table TSYS_MODULE add constraint FK_T_M_1 foreign key (menuId)
      references TSYS_MENU (menuId) on delete restrict on update restrict;

alter table TSYS_MODULE_HELP add constraint FK_T_MH_1 foreign key (helpId)
      references TSYS_MODULE (moduleId) on delete restrict on update restrict;

alter table TSYS_MODULE_URL add constraint FK_T_MU_1 foreign key (moduleId)
      references TSYS_MODULE (moduleId) on delete restrict on update restrict;

alter table TSYS_ROLE_POPEDOM add constraint FK_T_RP_1 foreign key (roleId)
      references TSYS_ROLE (roleId) on delete restrict on update restrict;

alter table TSYS_ROLE_POPEDOM add constraint FK_T_RP_2 foreign key (moduleId)
      references TSYS_MODULE (moduleId) on delete restrict on update restrict;

alter table TSYS_SYSTEM_ACCESS_LOG add constraint FK_T_SAL_1 foreign key (accountId)
      references TSYS_ACCOUNT (accountId) on delete restrict on update restrict;
