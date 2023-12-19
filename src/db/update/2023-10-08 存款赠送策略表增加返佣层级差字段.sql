ALTER TABLE "public"."station_deposit_strategy" ADD COLUMN "back_bonus_level_diff" numeric(16,2);
COMMENT ON COLUMN "public"."station_deposit_strategy"."back_bonus_level_diff" IS '返佣层级差';