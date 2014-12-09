-- Table: registro

-- DROP TABLE registro;

CREATE TABLE registro
(
  id serial NOT NULL,
   origen character varying(16),
  tipo character varying(3),
  data date,
  hora time without time zone,
  codigo bigint,
  CONSTRAINT ponto_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE registro
  OWNER TO postgres;
