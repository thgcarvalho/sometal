-- Table: ponto

-- DROP TABLE ponto;

CREATE TABLE ponto
(
  id serial NOT NULL,
  codigo bigint,
  data date,
  hora time without time zone,
  tipo character varying(3),
  origen character varying(16),
  CONSTRAINT ponto_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ponto
  OWNER TO postgres;
