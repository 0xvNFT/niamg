

DROP TABLE IF EXISTS "public"."tron_trans_user_task";
DROP SEQUENCE IF EXISTS "public"."tron_trans_user_task_seq";
CREATE SEQUENCE "public"."tron_trans_user_task_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."tron_trans_user_task" (
  "id" int4 NOT NULL DEFAULT nextval('tron_trans_user_task_seq'::regclass),
  "trans_from" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "trans_to" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "trans_value" float8 NOT NULL,
  "block" int8 NOT NULL,
  "transaction_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "transaction_type" int4 NOT NULL,
  "transaction_timestamp" int8 NOT NULL,
  "create_datetime" timestamp(6),
  "station_id" int4
)
;
ALTER TABLE "public"."tron_trans_user_task" OWNER TO "postgres";
COMMENT ON COLUMN "public"."tron_trans_user_task"."trans_from" IS '交易地址';
COMMENT ON COLUMN "public"."tron_trans_user_task"."trans_to" IS '交易地址';
COMMENT ON COLUMN "public"."tron_trans_user_task"."trans_value" IS '交易金额';
COMMENT ON COLUMN "public"."tron_trans_user_task"."block" IS '区块';
COMMENT ON COLUMN "public"."tron_trans_user_task"."transaction_id" IS '交易ID';
COMMENT ON COLUMN "public"."tron_trans_user_task"."transaction_type" IS '转账类型（1trx 2usdt）';
COMMENT ON COLUMN "public"."tron_trans_user_task"."transaction_timestamp" IS '转账时间';
COMMENT ON COLUMN "public"."tron_trans_user_task"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."tron_trans_user_task"."station_id" IS '站点id';
COMMENT ON TABLE "public"."tron_trans_user_task" IS 'tron链事件表';
