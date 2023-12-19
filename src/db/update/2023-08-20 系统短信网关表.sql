
-- ----------------------------
-- Table structure for system_sms_config
-- ----------------------------
CREATE SEQUENCE "public"."system_sms_config_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
DROP TABLE IF EXISTS "public"."system_sms_config";
CREATE TABLE "public"."system_sms_config" (
      "id" int4 NOT NULL DEFAULT nextval('system_sms_config_id_seq'::regclass) PRIMARY KEY,
      "country_code" varchar(10) COLLATE "pg_catalog"."default",
      "language" varchar(10) COLLATE "pg_catalog"."default",
      "country" varchar(100) COLLATE "pg_catalog"."default",
      "app_key" varchar(50) COLLATE "pg_catalog"."default",
      "app_secret" varchar(100) COLLATE "pg_catalog"."default",
      "app_code" varchar(50) COLLATE "pg_catalog"."default",
      "content" varchar(1000) COLLATE "pg_catalog"."default",
      "password" varchar(50) COLLATE "pg_catalog"."default",
      "account" varchar(50) COLLATE "pg_catalog"."default",
      "gateway_url" varchar(100) COLLATE "pg_catalog"."default",
      "status" int2
)
;
ALTER TABLE "public"."system_sms_config" OWNER TO "postgres";
COMMENT ON COLUMN "public"."system_sms_config"."country_code" IS '国家代号';
COMMENT ON COLUMN "public"."system_sms_config"."language" IS '语种代号';
COMMENT ON COLUMN "public"."system_sms_config"."country" IS '国家名称';
COMMENT ON COLUMN "public"."system_sms_config"."content" IS '短信内容模板';
COMMENT ON COLUMN "public"."system_sms_config"."password" IS '网关后台登录密码';
COMMENT ON COLUMN "public"."system_sms_config"."account" IS '网关后台登录帐号';
COMMENT ON COLUMN "public"."system_sms_config"."gateway_url" IS '网关请求地址';

delete from public.system_sms_config;
INSERT INTO "public"."system_sms_config"("id", "country_code", "country", "language","app_key", "app_secret", "app_code", "content", "password","account","gateway_url","status")
VALUES (1, 'MX', '墨西哥', 'es', '9CSFNO', 'OkQEiw', '1000','Your verification code is:{{}}','zvBO5m','9CSFNO','http://47.242.85.7:9090/sms/batch/v2',2);
INSERT INTO "public"."system_sms_config"("id", "country_code", "country", "language","app_key", "app_secret", "app_code", "content", "password","account","gateway_url","status")
VALUES (2, 'BR', '巴西', 'br', 'SlcWWI', 'LYFTpN', '1000','Your verification code is:{{}}','BlB9ov','SlcWWI','http://47.242.85.7:9090/sms/batch/v2',2);
INSERT INTO "public"."system_sms_config"("id", "country_code", "country", "language","app_key", "app_secret", "app_code", "content", "password","account","gateway_url","status")
VALUES (3, 'VN', '越南', 'vi', 'FW4LdY', 'MQ1LQ4', '1000','Your verification code is:{{}}','W3jOGE','FW4LdY','http://47.242.85.7:9090/sms/batch/v2',2);
INSERT INTO "public"."system_sms_config"("id", "country_code", "country", "language","app_key", "app_secret", "app_code", "content", "password","account","gateway_url","status")
VALUES (4, 'IN', '印度', 'in', 'pQLCNm', 'O3bMlP', '1000','Your verification code is:{{}}','tnXab8','pQLCNm','http://47.242.85.7:9090/sms/batch/v2',2);

delete from public.manager_menu where id=208;
delete from public.manager_menu where id=209;
delete from public.manager_menu where id=210;
delete from public.manager_menu where id=211;
INSERT INTO "public"."manager_menu"("id", "title", "url", "parent_id", "sort_no", "icon", "type", "perm_name") VALUES (208, '短信网关管理', '/sysGatewayConfig/index.do', 1, 5, NULL, 3, 'zk:sysGatewayConfig');
INSERT INTO "public"."manager_menu"("id", "title", "url", "parent_id", "sort_no", "icon", "type", "perm_name") VALUES (209, '修改', NULL, 208, 1, NULL, 4, 'zk:sysGatewayConfig:update');
INSERT INTO "public"."manager_menu"("id", "title", "url", "parent_id", "sort_no", "icon", "type", "perm_name") VALUES (210, '修改状态', NULL, 208, 2, NULL, 4, 'zk:sysGatewayConfig:status');
INSERT INTO "public"."manager_menu"("id", "title", "url", "parent_id", "sort_no", "icon", "type", "perm_name") VALUES (211, '删除', NULL, 208, 4, NULL, 4, 'zk:sysGatewayConfig:del');
