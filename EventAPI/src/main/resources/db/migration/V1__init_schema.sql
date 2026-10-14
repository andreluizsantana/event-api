CREATE SEQUENCE public.convidado_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.evento_auditoria_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.evento_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.fornecedor_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.item_evento_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.revinfo_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.convidados (
    id bigint NOT NULL,
    confirmado boolean NOT NULL,
    email character varying(200),
    nome character varying(150) NOT NULL,
    telefone character varying(20),
    evento_id bigint NOT NULL
);

CREATE TABLE public.convidados_aud (
    id bigint NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    confirmado boolean,
    email character varying(200),
    nome character varying(150),
    telefone character varying(20),
    evento_id bigint
);

CREATE TABLE public.eventos (
    id bigint NOT NULL,
    atualizado_em timestamp(6) without time zone,
    criado_em timestamp(6) without time zone NOT NULL,
    custo_evento numeric(38,2),
    custo_medio_convidado numeric(38,2),
    descricao_evento text NOT NULL,
    cep character varying(9),
    cidade character varying(120),
    numero character varying(10),
    referencia character varying(120),
    rua character varying(200),
    uf character varying(2),
    previsao_fim timestamp(6) without time zone NOT NULL,
    previsao_inicio timestamp(6) without time zone NOT NULL,
    status character varying(60) NOT NULL,
    titulo character varying(120) NOT NULL,
    CONSTRAINT eventos_status_check CHECK (((status)::text = ANY ((ARRAY['EM_ANDAMENTO'::character varying, 'PENDENTE'::character varying, 'CONCLUIDO'::character varying, 'CANCELADO'::character varying])::text[])))
);

CREATE TABLE public.eventos_aud (
    id bigint NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    custo_evento numeric(38,2),
    custo_medio_convidado numeric(38,2),
    descricao_evento text,
    cep character varying(9),
    cidade character varying(120),
    numero character varying(10),
    referencia character varying(120),
    rua character varying(200),
    uf character varying(2),
    previsao_fim timestamp(6) without time zone,
    previsao_inicio timestamp(6) without time zone,
    status character varying(60),
    titulo character varying(120),
    CONSTRAINT eventos_aud_status_check CHECK (((status)::text = ANY ((ARRAY['EM_ANDAMENTO'::character varying, 'PENDENTE'::character varying, 'CONCLUIDO'::character varying, 'CANCELADO'::character varying, 'AGENDADO'::character varying])::text[])))
);

CREATE TABLE public.fornecedores (
    id bigint NOT NULL,
    nome character varying(150) NOT NULL,
    servico character varying(200),
    valor_contrato numeric(38,2),
    evento_id bigint NOT NULL
);

CREATE TABLE public.fornecedores_aud (
    id bigint NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    nome character varying(150),
    servico character varying(200),
    valor_contrato numeric(38,2),
    evento_id bigint
);

CREATE TABLE public.item_evento (
    id bigint NOT NULL,
    descricao_item character varying(120) NOT NULL,
    valor_item numeric(38,2),
    evento_id bigint NOT NULL
);

CREATE TABLE public.item_evento_aud (
    id bigint NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    descricao_item character varying(120),
    valor_item numeric(38,2),
    evento_id bigint
);

CREATE TABLE public.lista_convidados (
    convidado_id bigint NOT NULL,
    lista_id bigint NOT NULL,
    data_adicao timestamp(6) without time zone,
    presente_confirmado boolean
);

CREATE TABLE public.lista_convidados_aud (
    convidado_id bigint NOT NULL,
    lista_id bigint NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    data_adicao timestamp(6) without time zone,
    presente_confirmado boolean
);

CREATE TABLE public.revinfo (
    id integer NOT NULL,
    "timestamp" bigint NOT NULL,
    usuario character varying(100)
);

ALTER TABLE ONLY public.convidados_aud
    ADD CONSTRAINT convidados_aud_pkey PRIMARY KEY (rev, id);

ALTER TABLE ONLY public.convidados
    ADD CONSTRAINT convidados_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.eventos_aud
    ADD CONSTRAINT eventos_aud_pkey PRIMARY KEY (rev, id);

ALTER TABLE ONLY public.eventos
    ADD CONSTRAINT eventos_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.fornecedores_aud
    ADD CONSTRAINT fornecedores_aud_pkey PRIMARY KEY (rev, id);

ALTER TABLE ONLY public.fornecedores
    ADD CONSTRAINT fornecedores_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.item_evento_aud
    ADD CONSTRAINT item_evento_aud_pkey PRIMARY KEY (rev, id);

ALTER TABLE ONLY public.item_evento
    ADD CONSTRAINT item_evento_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.lista_convidados_aud
    ADD CONSTRAINT lista_convidados_aud_pkey PRIMARY KEY (rev, convidado_id, lista_id);

ALTER TABLE ONLY public.lista_convidados
    ADD CONSTRAINT lista_convidados_pkey PRIMARY KEY (convidado_id, lista_id);

ALTER TABLE ONLY public.revinfo
    ADD CONSTRAINT revinfo_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.fornecedores
    ADD CONSTRAINT fk24eqhmpt2874lo0h04dki7qq7 FOREIGN KEY (evento_id) REFERENCES public.eventos(id);

ALTER TABLE ONLY public.fornecedores_aud
    ADD CONSTRAINT fk2btkeyskxiawb9xo4i207dls1 FOREIGN KEY (rev) REFERENCES public.revinfo(id);

ALTER TABLE ONLY public.item_evento
    ADD CONSTRAINT fkbt0lygdesnj3osa42q0kegk5r FOREIGN KEY (evento_id) REFERENCES public.eventos(id);

ALTER TABLE ONLY public.eventos_aud
    ADD CONSTRAINT fkg3fmyaippkd3pp8uf2nkcqc6t FOREIGN KEY (rev) REFERENCES public.revinfo(id);

ALTER TABLE ONLY public.lista_convidados
    ADD CONSTRAINT fkg5rbe2obf5udd3ie3sr96e0wk FOREIGN KEY (convidado_id) REFERENCES public.convidados(id);

ALTER TABLE ONLY public.item_evento_aud
    ADD CONSTRAINT fkh4vbkhw85s2hjqktnkwt45p5b FOREIGN KEY (rev) REFERENCES public.revinfo(id);

ALTER TABLE ONLY public.convidados_aud
    ADD CONSTRAINT fkkikrq4nlgyrhxmdyqp18xbike FOREIGN KEY (rev) REFERENCES public.revinfo(id);

ALTER TABLE ONLY public.lista_convidados_aud
    ADD CONSTRAINT fkkpw5xnn2qca2nnduqi9joir0p FOREIGN KEY (rev) REFERENCES public.revinfo(id);

ALTER TABLE ONLY public.convidados
    ADD CONSTRAINT fkpjw3mk5uwgemfo31t5hh5pc8k FOREIGN KEY (evento_id) REFERENCES public.eventos(id);
