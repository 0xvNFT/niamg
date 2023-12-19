ALTER TABLE "public"."station_deposit_strategy" ADD COLUMN "back_gift_type" int default 1;
COMMENT ON COLUMN "public"."station_deposit_strategy"."back_gift_type" IS '返佣赠送方式';