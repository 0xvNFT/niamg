ALTER TABLE "public"."sys_user_score_history" 
  ADD COLUMN "sign_count" int2;

COMMENT ON COLUMN "public"."sys_user_score_history"."sign_count" IS '连续签到次数';