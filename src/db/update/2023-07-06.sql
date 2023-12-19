	=============================JDB电子===============================

	-- 第三方游戏开关(每个站点都需要添加)
	-- 首先查询出有多少个站点 以及 站点的id 
	INSERT INTO "public".third_platform  (partner_id,station_id,status,platform)  SELECT partner_id,"id",2,120 FROM "public".station;