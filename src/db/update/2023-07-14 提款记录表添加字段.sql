ALTER TABLE "public"."mny_draw_record" ADD COLUMN "record_ip" varchar(50);
COMMENT ON COLUMN "public"."mny_draw_record"."record_ip" IS '提交记录时的ip';
ALTER TABLE "public"."mny_draw_record" ADD COLUMN "record_os" varchar(100)   DEFAULT NULL;
COMMENT ON COLUMN "public"."mny_draw_record"."record_os" IS  '提交记录时的设备操作系统';
ALTER TABLE "public"."mny_draw_record" ADD COLUMN "second_review" int default 1;
COMMENT ON COLUMN "public"."mny_draw_record"."second_review" IS '是否需要二次审核 2--需要 1--不需要';