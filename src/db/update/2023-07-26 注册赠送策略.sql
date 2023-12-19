
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (480, '注册赠送策略', 'admin.menu.registerStrategy', '/registerStrategy/index.do', 6, 4, '','admin:registerStrategy', 3);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (481, '修改', 'admin.menu.modify', '', 480, 3, '', 'admin:registerStrategy:modify', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (482, '删除', 'admin.menu.del', '', 480, 1, '', 'admin:registerStrategy:delete', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (483, '新增', 'admin.menu.add', '', 480, 2, '', 'admin:registerStrategy:add', 4);


CREATE SEQUENCE "public"."station_register_strategy_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
DROP TABLE IF EXISTS "public"."station_register_strategy";
CREATE TABLE "public"."station_register_strategy" (
      "id" int4 NOT NULL DEFAULT nextval('station_register_strategy_id_seq'::regclass) PRIMARY KEY,
      "station_id" int4,
      "gift_type" int2,
      "value_type" int2,
      "gift_value" numeric(16,2),
      "bet_rate" numeric(10,3),
      "upper_limit" numeric(16,4),
      "create_datetime" timestamp(6),
      "start_datetime" timestamp(6),
      "end_datetime" timestamp(6),
      "remark" varchar(200) COLLATE "pg_catalog"."default",
      "status" int2,
      "degree_ids" varchar(700) COLLATE "pg_catalog"."default",
      "group_ids" varchar(700) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."station_register_strategy" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_register_strategy"."gift_type" IS '赠送方式 1=固定数额 2=浮动比例';
COMMENT ON COLUMN "public"."station_register_strategy"."value_type" IS '赠送类型  1=彩金  2=积分';
COMMENT ON COLUMN "public"."station_register_strategy"."gift_value" IS '赠送额度';
COMMENT ON COLUMN "public"."station_register_strategy"."bet_rate" IS '打码量倍数。(赠送)x流水倍数=出款需要达到的投注量';
COMMENT ON COLUMN "public"."station_register_strategy"."upper_limit" IS '活动期间赠送上限';
COMMENT ON COLUMN "public"."station_register_strategy"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."station_register_strategy"."start_datetime" IS '开始时间';
COMMENT ON COLUMN "public"."station_register_strategy"."end_datetime" IS '截止时间';
COMMENT ON COLUMN "public"."station_register_strategy"."remark" IS '备注';
COMMENT ON COLUMN "public"."station_register_strategy"."status" IS '状态 1=禁用，2=启用';
COMMENT ON COLUMN "public"."station_register_strategy"."degree_ids" IS '有效会员等级id 多个以,分割';
COMMENT ON COLUMN "public"."station_register_strategy"."group_ids" IS '有效会员组别id 多个以,分割';
COMMENT ON TABLE "public"."station_register_strategy" IS '用户充值赠送策略';
