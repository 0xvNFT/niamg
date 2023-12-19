--请根据实际情况 替换其中参数
UPDATE "public"."station_kickback_strategy" SET kickback =3.0 WHERE station_id =144;
UPDATE "public"."sys_user_rebate" SET live =3, egame =3, chess =3,esport =3,sport =3,fishing=3,lottery=3 WHERE station_id =144;
UPDATE "public"."station_promotion" SET  live =3, egame =3, chess =3,esport =3,sport =3,fishing=3,lottery=3  WHERE station_id = 144;