
ALTER TABLE station_deposit_bank ADD COLUMN "deposit_rate" numeric(20,2);
COMMENT ON COLUMN "public"."station_deposit_bank"."deposit_rate" IS '存款汇率';

ALTER TABLE station_deposit_bank ADD COLUMN "withdraw_rate" numeric(20,2);
COMMENT ON COLUMN "public"."station_deposit_bank"."withdraw_rate" IS '取款汇率';