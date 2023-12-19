--pay_platform_no 增加平台订单号属性,trustpay需要这个属性
ALTER TABLE IF EXISTS public.mny_draw_record
    ADD COLUMN pay_platform_no character varying(200);