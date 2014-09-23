-- Table: funcionarios

-- DROP TABLE funcionarios;

CREATE TABLE funcionarios
(
  id serial NOT NULL,
  codigo integer NOT NULL,
  nome character varying(60),
  foto character varying(250),
  cargo character varying(60),
  salario numeric(10,2),
  admissao date,
  saida date,
  cpf character varying(14),
  pis character varying(15),
  endereco character varying(100),
  fone1 character varying(14),
  fone2 character varying(14),
  email character varying(60),
  banco character varying(45),
  agencia character varying(45),
  conta character varying(45),
  tipo character varying(45),
  obs character varying(500),
  CONSTRAINT funcionarios_pkey PRIMARY KEY (id),
  CONSTRAINT funcionarios_codigo_key UNIQUE (codigo)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE funcionarios
  OWNER TO postgres;
