ALTER TABLE public.eventos_aud ADD COLUMN revend integer;
ALTER TABLE public.eventos_aud ADD COLUMN revend_tstmp timestamp(6) without time zone;

ALTER TABLE public.convidados_aud ADD COLUMN revend integer;
ALTER TABLE public.convidados_aud ADD COLUMN revend_tstmp timestamp(6) without time zone;

ALTER TABLE public.fornecedores_aud ADD COLUMN revend integer;
ALTER TABLE public.fornecedores_aud ADD COLUMN revend_tstmp timestamp(6) without time zone;

ALTER TABLE public.item_evento_aud ADD COLUMN revend integer;
ALTER TABLE public.item_evento_aud ADD COLUMN revend_tstmp timestamp(6) without time zone;

ALTER TABLE public.lista_convidados_aud ADD COLUMN revend integer;
ALTER TABLE public.lista_convidados_aud ADD COLUMN revend_tstmp timestamp(6) without time zone;
