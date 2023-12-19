
ALTER TABLE "public"."mny_deposit_record" RENAME COLUMN "num" TO "deposit_virtual_coin_num";
ALTER TABLE "public"."mny_deposit_record" ALTER COLUMN deposit_virtual_coin_num TYPE numeric(20,2);
COMMENT ON COLUMN "public"."mny_deposit_record"."deposit_virtual_coin_num" IS '存款虚拟币数量';

ALTER TABLE "public"."mny_deposit_record" RENAME COLUMN "rate" TO "deposit_virtual_coin_rate";
ALTER TABLE "public"."mny_deposit_record" ALTER COLUMN deposit_virtual_coin_rate TYPE numeric(20,2);
COMMENT ON COLUMN "public"."mny_deposit_record"."deposit_virtual_coin_rate" IS '存款虚拟币汇率';

ALTER TABLE "public"."mny_draw_record" ADD COLUMN "withdraw_virtual_coin_num" numeric(20,2);
COMMENT ON COLUMN "public"."mny_draw_record"."withdraw_virtual_coin_num" IS '取款虚拟币数量';

ALTER TABLE "public"."mny_draw_record" ADD COLUMN "withdraw_virtual_coin_rate" numeric(20,2);
COMMENT ON COLUMN "public"."mny_draw_record"."withdraw_virtual_coin_rate" IS '取款虚拟币汇率';

UPDATE "public"."station_config" SET title = 'USDT存款汇率' WHERE key = 'pay_tips_deposit_usdt_rate';