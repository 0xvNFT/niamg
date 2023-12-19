

-- Add Popup Status for information Bulletin
ALTER TABLE station_article ADD COLUMN popup_status int2;
COMMENT ON COLUMN "public"."station_article"."popup_status" IS '弹出状态 1:禁用  2:启用';