CREATE SEQUENCE "public"."app_tab_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
-- ----------------------------
--  Table structure for app_tab
-- ----------------------------
DROP TABLE IF EXISTS "public"."app_tab";
CREATE TABLE "public"."app_tab" (
    "id" int4 NOT NULL DEFAULT nextval('app_tab_id_seq'::regclass) PRIMARY KEY,
	"station_id" int4,
	"name" varchar COLLATE "default",
	"code" varchar COLLATE "default",
	"sort_no" int4,
	"create_time" timestamp(6),
	"icon" varchar COLLATE "default",
	"status" int2 default 2,
	"type" int2
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."app_tab" OWNER TO "postgres";

COMMENT ON COLUMN "public"."app_tab"."type" IS '游戏标签类型';
CREATE UNIQUE INDEX "station_id_sc" ON "public"."app_tab" ("station_id","code");


INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (320, '主页标签管理', 'admin.app.tab', '/appTab/index.do', 10, 12, '', 'admin.app.tab', 3);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (321, '新增', 'admin.menu.add', '', 320, 10, '', 'admin.app.tab:add', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (322, '修改', 'admin.menu.modify', '', 320, 8, '', 'admin.app.tab:modify', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (323, '删除', 'admin.menu.del', '', 320, 2, '', 'admin.app.tab:delete', 4);
