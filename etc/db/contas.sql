-- Table: contas

-- DROP TABLE contas;

CREATE TABLE contas
(
  id serial NOT NULL,
  nome character varying(60) NOT NULL,
  valor numeric(10,2),
  vencimento date,
  situacao character varying(45),
  obs character varying(250),
  CONSTRAINT conta_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE contas
  OWNER TO postgres;
