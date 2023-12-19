ALTER TABLE "public"."station_register_strategy" ADD COLUMN "back_gift_type" int default 1;
COMMENT ON COLUMN "public"."station_register_strategy"."back_gift_type" IS '返佣赠送方式';
ALTER TABLE "public"."station_register_strategy" ADD COLUMN "bonus_back_value" numeric(16,2);
COMMENT ON COLUMN "public"."station_register_strategy"."bonus_back_value" IS '给上级代理/推荐人返佣的额度';
ALTER TABLE "public"."station_register_strategy" ADD COLUMN "bonus_back_bet_rate" numeric(10,3);
COMMENT ON COLUMN "public"."station_register_strategy"."bonus_back_bet_rate" IS '上级代理/推荐人得到返佣额度时的出款所需打码量倍数';