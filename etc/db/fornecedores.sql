-- Table: fornecedores

-- DROP TABLE fornecedores;

CREATE TABLE fornecedores
(
  id serial NOT NULL,
  nome character varying(60) NOT NULL,
  cadastro character varying(18),
  fone1 character varying(14),
  fone2 character varying(14),
  email character varying(45),
  endereco character varying(45),
  numero character varying(16),
  complemento character varying(45),
  cep character varying(10),
  bairro character varying(45),
  cidade character varying(45),
  banco character varying(45),
  agencia character varying(16),
  conta character varying(16),
  tipo character varying(45),
  obs character varying(250),
  oferta character varying(1000),
  CONSTRAINT fornecedores_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE fornecedores
  OWNER TO postgres;
