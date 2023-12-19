ALTER TABLE "public"."sys_user_daily_money" 
  ADD COLUMN "recommend_id" int4;

COMMENT ON COLUMN "public"."sys_user_daily_money"."recommend_id" IS '推荐者id';

ALTER TABLE "public"."sys_user_money_history" 
  ADD COLUMN "recommend_id" int4;

COMMENT ON COLUMN "public"."sys_user_money_history"."recommend_id" IS '推荐者id';

ALTER TABLE "public"."mny_deposit_record" 
  ADD COLUMN "recommend_id" int4;

COMMENT ON COLUMN "public"."mny_deposit_record"."recommend_id" IS '推荐者id';


ALTER TABLE "public"."mny_draw_record" 
  ADD COLUMN "recommend_id" int4;

COMMENT ON COLUMN "public"."mny_draw_record"."recommend_id" IS '推荐者id';



ALTER TABLE "public"."station_red_packet" 
  ADD COLUMN "group_ids" varchar(700);

COMMENT ON COLUMN "public"."station_red_packet"."group_ids" IS '有效会员组别id 多个以,分割';

ALTER TABLE "public"."station_deposit_strategy" 
  ADD COLUMN "group_ids" varchar(700);
COMMENT ON COLUMN "public"."station_deposit_strategy"."group_ids" IS '有效会员组别id 多个以,分割';
