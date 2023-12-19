ALTER TABLE "public"."sys_user" ADD COLUMN "email" varchar(100);
COMMENT ON COLUMN "public"."sys_user"."email" IS '邮件地址';
ALTER TABLE "public"."sys_user_info" ADD COLUMN "neck_name" varchar(100);
COMMENT ON COLUMN "public"."sys_user_info"."neck_name" IS '昵称';