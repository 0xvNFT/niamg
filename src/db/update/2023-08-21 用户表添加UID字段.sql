ALTER TABLE "public"."sys_user" ADD COLUMN "uid" varchar(30);
COMMENT ON COLUMN "public"."sys_user"."uid" IS '用户UID';