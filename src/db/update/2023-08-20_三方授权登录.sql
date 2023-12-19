DROP TABLE IF EXISTS "public"."sys_user_third_auth";
DROP SEQUENCE IF EXISTS "public"."sys_user_third_auth_id_seq";
CREATE SEQUENCE "public"."sys_user_third_auth_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_user_third_auth" (
    "id" int8 NOT NULL DEFAULT nextval('sys_user_third_auth_id_seq'::regclass)PRIMARY KEY,
    "station_id" int8,
    "user_id" int8,
    "local_username" varchar(256),
    "source" varchar(64),
    "third_id" varchar(256),
    "username" varchar(256),
    "nickname" varchar(256),
    "gender" varchar(64),
    "avatar" text,
    "blog" text,
    "company" varchar(512),
    "location" varchar(128),
    "email" varchar(256),
    "remark" text,
    "create_datetime" timestamp(6)
)WITH (OIDS=FALSE);

ALTER TABLE "public"."sys_user_third_auth" OWNER TO "postgres";

COMMENT ON COLUMN "public"."sys_user_third_auth"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_user_third_auth"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."sys_user_third_auth"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_third_auth"."local_username" IS '本地用户名';
COMMENT ON COLUMN "public"."sys_user_third_auth"."source" IS '授权平台来源（Google、Facebook...）';
COMMENT ON COLUMN "public"."sys_user_third_auth"."third_id" IS '用户第三方系统的唯一ID（third_id + source 可以唯一确定一个用户）';
COMMENT ON COLUMN "public"."sys_user_third_auth"."username" IS '第三方用户名';
COMMENT ON COLUMN "public"."sys_user_third_auth"."nickname" IS '用户昵称';
COMMENT ON COLUMN "public"."sys_user_third_auth"."gender" IS '性别';
COMMENT ON COLUMN "public"."sys_user_third_auth"."avatar" IS '用户头像';
COMMENT ON COLUMN "public"."sys_user_third_auth"."blog" IS '用户网址';
COMMENT ON COLUMN "public"."sys_user_third_auth"."company" IS '所在公司';
COMMENT ON COLUMN "public"."sys_user_third_auth"."location" IS '位置';
COMMENT ON COLUMN "public"."sys_user_third_auth"."email" IS '用户邮箱';
COMMENT ON COLUMN "public"."sys_user_third_auth"."remark" IS '用户备注（各平台中的用户个人介绍）';
COMMENT ON COLUMN "public"."sys_user_third_auth"."create_datetime" IS '创建时间';
COMMENT ON TABLE "public"."sys_user_third_auth" IS '用户第三方授权信息';