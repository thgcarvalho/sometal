-- Table: viagens

-- DROP TABLE viagens;

CREATE TABLE viagens
(
  id serial NOT NULL,
  veiculo_id bigint NOT NULL,
  data date,
  motorista character varying(45),
  saida timestamp with time zone,
  km_saida numeric(10,0),
  chegada timestamp with time zone,
  km_chegada numeric(10,0),
  destino character varying(45),
  motivo character varying(45),
  obs character varying(500),
  CONSTRAINT viagens_pkey PRIMARY KEY (id),
  CONSTRAINT viagens_veiculo_id_fkey FOREIGN KEY (veiculo_id)
      REFERENCES veiculos (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE viagens
  OWNER TO postgres;
