ALTER TABLE "public"."sys_user_login" 
  ADD COLUMN "domain" varchar(500);

COMMENT ON COLUMN "public"."sys_user_login"."domain" IS '登录域名';