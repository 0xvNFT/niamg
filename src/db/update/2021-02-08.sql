ALTER TABLE "public"."station_kickback_record"
  ADD COLUMN "group_id" int4;

COMMENT ON COLUMN "public"."station_kickback_record"."group_id" IS '会员组别id';