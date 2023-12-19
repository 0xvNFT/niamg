
ALTER TABLE "public"."sys_user" ADD COLUMN "user_register_type" varchar(16);
COMMENT ON COLUMN "public"."sys_user"."user_register_type" IS '会员注册类型';