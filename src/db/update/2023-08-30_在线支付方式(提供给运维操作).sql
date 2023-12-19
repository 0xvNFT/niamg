--merchant_code 属性长度需要增加到100,原来是36太短了
ALTER TABLE public.station_deposit_online
    ALTER COLUMN merchant_code TYPE character varying(100) COLLATE pg_catalog."default";