-- Table: viajens

-- DROP TABLE viajens;

CREATE TABLE viajens
(
  id serial NOT NULL,
  veiculo_id bigint NOT NULL,
  data date,
  motorista character varying(45),
  saida date,
  km_saida numeric(10,0),
  chegada date,
  km_chegada numeric(10,0),
  destino character varying(45),
  motivo character varying(45),
  obs character varying(500),
  CONSTRAINT viajens_pkey PRIMARY KEY (id),
  CONSTRAINT viajens_veiculo_id_fkey FOREIGN KEY (veiculo_id)
      REFERENCES veiculos (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE viajens
  OWNER TO postgres;
