

DROP TABLE IF EXISTS "public"."tron_trans_index";
DROP SEQUENCE IF EXISTS "public"."tron_trans_index_id_seq";
CREATE SEQUENCE "public"."tron_trans_index_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."tron_trans_index" (
    "id" int8 NOT NULL DEFAULT nextval('tron_trans_index_id_seq'::regclass)PRIMARY KEY,
    "last_timestamp" timestamp(6),
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
    "last_timestamp" timestamp(6),
    "tron_link_addr" varchar(255) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);

ALTER TABLE "public"."tron_trans_index_address" OWNER TO "postgres";

COMMENT ON COLUMN "public"."tron_trans_index_address"."last_timestamp" IS '最后执行到时间点';
COMMENT ON COLUMN "public"."tron_trans_index_address"."tron_link_addr" IS 'Tron链地址';
COMMENT ON TABLE "public"."tron_trans_index_address" IS '站点转入第三方限额表';