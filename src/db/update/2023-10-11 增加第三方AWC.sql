-- AE真人 第三方游戏开关(每个站点都需要添加)
INSERT INTO "public".third_platform  (partner_id,station_id,status,platform)  SELECT partner_id,"id",1,170 FROM "public".station;