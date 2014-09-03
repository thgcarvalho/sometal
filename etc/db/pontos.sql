-- Table: pontos

-- DROP TABLE pontos;

CREATE TABLE pontos
(
  id serial NOT NULL,
  funcionario_id bigint,
  data date,
  entrada time without time zone,
  saida time without time zone,
  obs character varying(500),
  CONSTRAINT pontos_pkey PRIMARY KEY (id),
  CONSTRAINT pontos_funcionario_id_fkey FOREIGN KEY (funcionario_id)
      REFERENCES funcionarios (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE pontos
  OWNER TO postgres;
