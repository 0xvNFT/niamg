-- 不增加会报报错记录错误
ALTER TABLE public.mny_draw_record
    ALTER COLUMN record_os TYPE character varying(500) COLLATE pg_catalog."default";