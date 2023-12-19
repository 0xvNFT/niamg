
CREATE SEQUENCE "public"."admin_login_google_auth_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."admin_login_google_auth" (
	"id" int4 not null default nextval('admin_login_google_auth_id_seq') PRIMARY KEY,
	"station_id" int4 ,
	"key" varchar(50),
	"admin_username" varchar(2000),
	"remark" varchar(200),
	"status" int2 DEFAULT 2
);
COMMENT ON TABLE "public"."admin_login_google_auth" IS '谷歌验证信息';
COMMENT ON COLUMN "public"."admin_login_google_auth"."admin_username" IS '管理员账号';
COMMENT ON COLUMN "public"."admin_login_google_auth"."remark" IS '备注';
COMMENT ON COLUMN "public"."admin_login_google_auth"."status" IS '1=不验证，2=验证';

INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (324, '谷歌验证管理', 'admin.googleAuth', '/googleAuth/index.do', 8, 8, '', 'admin:googleAuth', 3);


INSERT INTO "public"."manager_menu"("id", "title", "url", "parent_id", "sort_no", "icon", "type", "perm_name") VALUES (105, '谷歌验证管理', '/googleAuthConfig/index.do', 3, 3, NULL, 3, 'zk:googleAuthConfig');
INSERT INTO "public"."manager_menu"("id", "title", "url", "parent_id", "sort_no", "icon", "type", "perm_name") VALUES (106, '修改状态', NULL, 105, 5, NULL, 4, 'zk:googleAuthConfig:status');
INSERT INTO "public"."manager_menu"("id", "title", "url", "parent_id", "sort_no", "icon", "type", "perm_name") VALUES (107, '删除', NULL, 105, 4, NULL, 4, 'zk:googleAuthConfig:del');
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (325, '国家/地区白名单', 'admin.CountryCitywhitelist', '/stationWhiteArea/index.do', 8, 11, '', 'admin:stationAreaWhiteIp', 3);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (326, '修改', 'admin.menu.modify', '', 325, 3, '', 'admin:stationAreaWhiteIp:modify', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (327, '新增', 'admin.menu.add', '', 325, 2, '', 'admin:stationAreaWhiteIp:add', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (328, '删除', 'admin.menu.del', '', 325, 1, '', 'admin:stationAreaWhiteIp:delete', 4);