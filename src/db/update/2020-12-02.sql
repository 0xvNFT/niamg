CREATE SEQUENCE "public"."station_bank_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_bank" (
  "id" int4 NOT NULL DEFAULT nextval('station_bank_id_seq'::regclass) PRIMARY KEY,
  "name" varchar(200) COLLATE "pg_catalog"."default",
  "code" varchar(50) COLLATE "pg_catalog"."default",
  "sort_no" int4,
  "station_id" int4,
  "create_time" timestamp(6)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_bank" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_bank"."name" IS '银行名称';
COMMENT ON COLUMN "public"."station_bank"."code" IS '编号';
COMMENT ON COLUMN "public"."station_bank"."sort_no" IS '排序序号，大的排前面';
COMMENT ON TABLE "public"."station_bank" IS '站点银行信息表';
CREATE UNIQUE INDEX "station_bank_sc" ON "public"."station_bank" ("station_id","code");


INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (306, '银行资料管理', 'admin.menu.bankConfig', '/bankConfig/index.do', 7, 12, '', 'admin:bankConfig', 3);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (307, '新增', 'admin.menu.add', '', 306, 10, '', 'admin:bankConfig:add', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (308, '修改', 'admin.menu.modify', '', 306, 8, '', 'admin:bankConfig:modify', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (309, '删除', 'admin.menu.del', '', 306, 2, '', 'admin:bankConfig:delete', 4);