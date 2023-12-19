drop table sys_user;
drop table sys_user_register;
CREATE TABLE "public"."sys_user" (
  "id" int4 NOT NULL DEFAULT nextval('sys_user_id_seq'::regclass) PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "agent_id" int4 DEFAULT 0,
  "agent_username" varchar(50) COLLATE "pg_catalog"."default",
  "proxy_id" int4 DEFAULT 0,
  "proxy_name" varchar(50) COLLATE "pg_catalog"."default",
  "parent_ids" text COLLATE "pg_catalog"."default",
  "parent_names" text COLLATE "pg_catalog"."default",
  "recommend_id" int4 DEFAULT 0,
  "recommend_username" varchar(50) COLLATE "pg_catalog"."default",
  "degree_id" int4,
  "group_id" int4,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "password" varchar(200) COLLATE "pg_catalog"."default",
  "salt" varchar(20) COLLATE "pg_catalog"."default",
  "type" int2,
  "create_datetime" timestamp(6),
  "create_user_id" int4,
  "create_username" int4,
  "status" int2,
  "remark" text COLLATE "pg_catalog"."default",
  "lock_degree" int2,
  "level" int2,
  "online_warn" int4,
  "update_pwd_datetime" timestamp(6),
  "money_status" int2,
  "register_ip" varchar(100) COLLATE "default",
  "register_url" varchar(255) COLLATE "pg_catalog"."default",
  "register_os" varchar(50) COLLATE "pg_catalog"."default",
  "agent_promo_code" varchar(20) COLLATE "pg_catalog"."default",
  "proxy_promo_code" varchar(20) COLLATE "pg_catalog"."default",
  "recommend_promo_code" varchar(20) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user" OWNER TO "postgres";
CREATE UNIQUE INDEX "sys_user_unkey" ON "public"."sys_user" USING btree ("username","station_id");
CREATE INDEX "sys_user_cdt" ON "public"."sys_user" USING btree ("create_datetime");
COMMENT ON COLUMN "public"."sys_user"."partner_id" IS '合作商ID';
COMMENT ON COLUMN "public"."sys_user"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."sys_user"."agent_id" IS '代理商ID';
COMMENT ON COLUMN "public"."sys_user"."agent_username" IS '代理商账号';
COMMENT ON COLUMN "public"."sys_user"."proxy_id" IS '上级代理ID';
COMMENT ON COLUMN "public"."sys_user"."proxy_name" IS '上级代理账号';
COMMENT ON COLUMN "public"."sys_user"."parent_ids" IS '所有代理上级，以逗号隔开(,1,2,3,)';
COMMENT ON COLUMN "public"."sys_user"."parent_names" IS '所有上级代理账号，以逗号隔开(,a,b,c,)';
COMMENT ON COLUMN "public"."sys_user"."recommend_id" IS '推荐会员ID';
COMMENT ON COLUMN "public"."sys_user"."recommend_username" IS '推荐会员账号';
COMMENT ON COLUMN "public"."sys_user"."degree_id" IS '会员等级ID';
COMMENT ON COLUMN "public"."sys_user"."group_id" IS '会员组别ID';
COMMENT ON COLUMN "public"."sys_user"."username" IS '账号';
COMMENT ON COLUMN "public"."sys_user"."password" IS '密码';
COMMENT ON COLUMN "public"."sys_user"."salt" IS '密码的盐';
COMMENT ON COLUMN "public"."sys_user"."type" IS '用户类型(UserTypeEnum的值)';
COMMENT ON COLUMN "public"."sys_user"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user"."create_user_id" IS '创建者ID';
COMMENT ON COLUMN "public"."sys_user"."create_username" IS '创建者账号';
COMMENT ON COLUMN "public"."sys_user"."status" IS '账号状态 1:禁用 2:启用';
COMMENT ON COLUMN "public"."sys_user"."remark" IS '备注内容';
COMMENT ON COLUMN "public"."sys_user"."lock_degree" IS '2=锁定会员等级，1=不锁定';
COMMENT ON COLUMN "public"."sys_user"."level" IS '会员层级';
COMMENT ON COLUMN "public"."sys_user"."online_warn" IS '在线告警';
COMMENT ON COLUMN "public"."sys_user"."update_pwd_datetime" IS '最后一次修改密码时间';
COMMENT ON COLUMN "public"."sys_user"."money_status" IS '账户余额状态，1=冻结，2=正常';
COMMENT ON COLUMN "public"."sys_user"."register_ip" IS '注册IP';
COMMENT ON COLUMN "public"."sys_user"."register_url" IS '注册URL';
COMMENT ON COLUMN "public"."sys_user"."register_os" IS '注册电脑或手机系统';
COMMENT ON COLUMN "public"."sys_user"."agent_promo_code" IS '站点下代理商推广码';
COMMENT ON COLUMN "public"."sys_user"."proxy_promo_code" IS '代理推广码';
COMMENT ON COLUMN "public"."sys_user"."recommend_promo_code" IS '会员推广码';
COMMENT ON TABLE "public"."sys_user" IS '会员账号信息';