INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (450, '提款规则管理', 'admin.menu.drawRuleStrategy', '/drawRuleStrategy/index.do', 7, 4, '','admin:drawRuleStrategy', 3);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (451, '修改', 'admin.menu.modify', '', 450, 3, '', 'admin:drawRuleStrategy:modify', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (452, '删除', 'admin.menu.del', '', 450, 1, '', 'admin:drawRuleStrategy:delete', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (453, '新增', 'admin.menu.add', '', 450, 2, '', 'admin:drawRuleStrategy:add', 4);


CREATE SEQUENCE "public"."station_draw_rule_strategy_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
DROP TABLE IF EXISTS "public"."station_draw_rule_strategy";
CREATE TABLE "public"."station_draw_rule_strategy" (
   "id" int4 NOT NULL DEFAULT nextval('station_draw_rule_strategy_id_seq'::regclass) PRIMARY KEY,
   "station_id" int4,
   "type" int2,
   "value" numeric(20,6),
   "rate" numeric(20,6),
   "remark" varchar(500) COLLATE "pg_catalog"."default",
   "degree_ids" varchar(700) COLLATE "pg_catalog"."default",
   "group_ids" varchar(700) COLLATE "pg_catalog"."default",
   "status" int2,
   "days" int2,
   "limit_country" varchar(30) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."station_draw_rule_strategy" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_draw_rule_strategy"."type" IS '规则类型';
COMMENT ON COLUMN "public"."station_draw_rule_strategy"."value" IS '设定值';
COMMENT ON COLUMN "public"."station_draw_rule_strategy"."rate" IS '比率 0-1';
COMMENT ON COLUMN "public"."station_draw_rule_strategy"."degree_ids" IS '有效会员等级id 多个以,分割';
COMMENT ON COLUMN "public"."station_draw_rule_strategy"."group_ids" IS '有效会员组别id 多个以,分割';
COMMENT ON COLUMN "public"."station_draw_rule_strategy"."status" IS '启用状态';
COMMENT ON COLUMN "public"."station_draw_rule_strategy"."days" IS '多少天内';
COMMENT ON COLUMN "public"."station_draw_rule_strategy"."limit_country" IS '限制国家代码';
COMMENT ON TABLE "public"."station_draw_rule_strategy" IS '站点每日最高在线统计';