ALTER TABLE public.eventos ADD COLUMN version bigint;
ALTER TABLE public.eventos ADD COLUMN criado_por character varying(100);
ALTER TABLE public.eventos ADD COLUMN atualizado_por character varying(100);

ALTER TABLE public.eventos DROP CONSTRAINT eventos_status_check;
ALTER TABLE public.eventos ADD CONSTRAINT eventos_status_check CHECK (((status)::text = ANY ((ARRAY['EM_ANDAMENTO'::character varying, 'PENDENTE'::character varying, 'CONCLUIDO'::character varying, 'CANCELADO'::character varying, 'AGENDADO'::character varying])::text[])));
