
delete from third_platform where platform not in(1,2,3,6,11,21,24,26,38,40);

ALTER TABLE "public"."third_trans_log" 
  ADD COLUMN "msg" varchar(500);

COMMENT ON COLUMN "public"."third_trans_log"."msg" IS '原因';


INSERT INTO third_platform(partner_id,station_id,status,platform) VALUES (0, 1, 2, 20);
INSERT INTO third_platform(partner_id,station_id,status,platform) VALUES (0, 2, 2, 20);
INSERT INTO third_platform(partner_id,station_id,status,platform) VALUES (0, 3, 2, 20);
INSERT INTO third_platform(partner_id,station_id,status,platform) VALUES (0, 4, 2, 20);
INSERT INTO third_platform(partner_id,station_id,status,platform) VALUES (0, 5, 2, 20);