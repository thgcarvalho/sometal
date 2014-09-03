-- Table: clientes

-- DROP TABLE clientes;

CREATE TABLE clientes
(
  id bigserial NOT NULL,
  nome character varying(45) NOT NULL,
  cadastro character varying(45),
  endereco character varying(45),
  bairro character varying(45),
  cidade character varying(45),
  cep character varying(45),
  numero character varying(45),
  complemento character varying(45),
  fone1 character varying(45),
  fone2 character varying(45),
  email character varying(45),
  obs character varying(500),
  CONSTRAINT clientes_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE clientes
  OWNER TO postgres;
