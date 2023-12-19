ALTER TABLE "public"."station_register_config"
  ADD COLUMN "code" varchar(50);

COMMENT ON COLUMN "public"."station_register_config"."code" IS '多语言的code';