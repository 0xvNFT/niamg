

ALTER TABLE "public"."sys_user_info" ADD COLUMN "line" VARCHAR(500) DEFAULT NULL;
COMMENT ON COLUMN "public"."sys_user_info"."line" IS 'Line账户';