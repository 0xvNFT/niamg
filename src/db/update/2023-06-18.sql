
DROP TABLE IF EXISTS "public"."station_tron_link_deposit";
DROP SEQUENCE IF EXISTS "public"."station_tron_link_deposit_id_seq";
CREATE SEQUENCE "public"."station_tron_link_deposit_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
DROP TABLE IF EXISTS "public"."station_tron_link_deposit";
CREATE TABLE "public"."station_tron_link_deposit" (
    "id" int8 NOT NULL DEFAULT nextval('station_tron_link_deposit_id_seq'::regclass)PRIMARY KEY,
    "station_id" int8,
    "tron_link_addr" varchar(512) COLLATE "pg_catalog"."default",
    "tron_link_status" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_tron_link_deposit" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_tron_link_deposit"."id" IS '唯一主键';
COMMENT ON COLUMN "public"."station_tron_link_deposit"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."station_tron_link_deposit"."tron_link_addr" IS 'Tron链地址';
COMMENT ON COLUMN "public"."station_tron_link_deposit"."tron_link_status" IS 'Tron链状态(1禁用,2启用)';
COMMENT ON TABLE "public"."station_tron_link_deposit" IS '站点Tron链存款配置';


-------------------------------------------------- admin_menu --------------------------------------------------
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (329, '站点存款Tron链管理', 'admin.menu.tronLink.deposit', '/tronLink/deposit/index.do', 7, 1, '', 'admin:tronLink:deposit', 3);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (330, '新增', 'admin.menu.add', '', 329, 1, '', 'admin:tronLink:deposit:add', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (331, '删除', 'admin.menu.del', '', 329, 2, '', 'admin:tronLink:deposit:delete', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (332, '修改状态', 'admin.menu.update.status', '', 329, 3, '', 'admin:tronLink:deposit:status', 4);



DROP TABLE IF EXISTS "public"."sys_user_tron_link";
DROP SEQUENCE IF EXISTS "public"."sys_user_tron_link_id_seq";
CREATE SEQUENCE "public"."sys_user_tron_link_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_user_tron_link" (
    "id" int8 NOT NULL DEFAULT nextval('sys_user_tron_link_id_seq'::regclass)PRIMARY KEY,
    "station_id" int8,
    "user_id" int8,
    "user_name" varchar(50),
    "tron_link_addr" varchar(512),
    "init_trx" numeric(16,4),
    "create_datetime" timestamp(6),
    "init_valid_datetime" timestamp(6),
    "bind_status" int2
)WITH (OIDS=FALSE);

ALTER TABLE "public"."sys_user_tron_link" OWNER TO "postgres";
CREATE UNIQUE INDEX "sys_user_tron_link_idx" ON "public"."sys_user_tron_link" USING btree ("tron_link_addr");
CREATE INDEX "sys_user_tron_link_ui_idx" ON "public"."sys_user_tron_link" USING btree ("user_id");
CREATE INDEX "sys_user_tron_link_ur_idx" ON "public"."sys_user_tron_link" USING btree ("user_name");

COMMENT ON COLUMN "public"."sys_user_tron_link"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_user_tron_link"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."sys_user_tron_link"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_tron_link"."user_name" IS '用户账号';
COMMENT ON COLUMN "public"."sys_user_tron_link"."tron_link_addr" IS 'Tron链地址';
COMMENT ON COLUMN "public"."sys_user_tron_link"."init_trx" IS '初始化trx金额';
COMMENT ON COLUMN "public"."sys_user_tron_link"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user_tron_link"."init_valid_datetime" IS '初始化转账最后时间';
COMMENT ON COLUMN "public"."sys_user_tron_link"."bind_status" IS '绑定状态（1未绑定 2已绑定）';
COMMENT ON TABLE "public"."sys_user_tron_link" IS '用户Tron链管理';


DROP TABLE IF EXISTS "public"."tron_trans_index";
DROP SEQUENCE IF EXISTS "public"."tron_trans_index_id_seq";
CREATE SEQUENCE "public"."tron_trans_index_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."tron_trans_index" (
    "id" int8 NOT NULL DEFAULT nextval('tron_trans_index_id_seq'::regclass)PRIMARY KEY,
    "last_timestamp" int8,
    "last_transaction_id" varchar(255) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);

ALTER TABLE "public"."tron_trans_index" OWNER TO "postgres";

COMMENT ON COLUMN "public"."tron_trans_index"."last_timestamp" IS '最后执行到时间点';
COMMENT ON COLUMN "public"."tron_trans_index"."last_transaction_id" IS '最后执行到的交易ID';
COMMENT ON TABLE "public"."tron_trans_index" IS '站点转入第三方限额表';


DROP TABLE IF EXISTS "public"."tron_trans_index_address";
DROP SEQUENCE IF EXISTS "public"."tron_trans_index_address_id_seq";
CREATE SEQUENCE "public"."tron_trans_index_address_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."tron_trans_index_address" (
    "id" int8 NOT NULL DEFAULT nextval('tron_trans_index_address_id_seq'::regclass)PRIMARY KEY,
    "last_timestamp" int8,
    "tron_link_addr" varchar(255) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);

ALTER TABLE "public"."tron_trans_index_address" OWNER TO "postgres";

COMMENT ON COLUMN "public"."tron_trans_index_address"."last_timestamp" IS '最后执行到时间点';
COMMENT ON COLUMN "public"."tron_trans_index_address"."tron_link_addr" IS 'Tron链地址';
COMMENT ON TABLE "public"."tron_trans_index_address" IS '站点转入第三方限额表';


DROP TABLE IF EXISTS "public"."tron_trans_user";
DROP SEQUENCE IF EXISTS "public"."tron_trans_user_id_seq";
CREATE SEQUENCE "public"."tron_trans_user_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."tron_trans_user" (
    "id" int8 NOT NULL DEFAULT nextval('tron_trans_user_id_seq'::regclass)PRIMARY KEY,
    "trans_from" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "trans_to" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "trans_value" float8 NOT NULL,
    "block" int8 NOT NULL,
    "transaction_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "transaction_type" int4 NOT NULL,
    "transaction_timestamp" int8 NOT NULL,
    "create_datetime" timestamp(6),
    "station_id" int4
)WITH (OIDS=FALSE);

ALTER TABLE "public"."tron_trans_user" OWNER TO "postgres";
CREATE UNIQUE INDEX "tron_usdt_trans_uti_idx" ON "public"."tron_trans_user" USING btree ("transaction_id");
CREATE INDEX "tron_usdt_trans_uf_idx" ON "public"."tron_trans_user" USING btree ("trans_from");
CREATE INDEX "tron_usdt_trans_ut_idx" ON "public"."tron_trans_user" USING btree ("trans_to");
CREATE INDEX "tron_usdt_create_datetime" ON "public"."tron_trans_user" USING btree ("create_datetime");
CREATE INDEX "tron_usdt_station_id" ON "public"."tron_trans_user" USING btree ("station_id");

COMMENT ON COLUMN "public"."tron_trans_user"."trans_from" IS '交易地址';
COMMENT ON COLUMN "public"."tron_trans_user"."trans_to" IS '交易地址';
COMMENT ON COLUMN "public"."tron_trans_user"."trans_value" IS '交易金额';
COMMENT ON COLUMN "public"."tron_trans_user"."block" IS '区块';
COMMENT ON COLUMN "public"."tron_trans_user"."transaction_id" IS '交易ID';
COMMENT ON COLUMN "public"."tron_trans_user"."transaction_type" IS '转账类型（1trx 2usdt）';
COMMENT ON COLUMN "public"."tron_trans_user"."transaction_timestamp" IS '转账时间';
COMMENT ON TABLE "public"."tron_trans_user" IS '站点转入第三方限额表';
