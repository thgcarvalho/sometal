-- Table: abastecimentos

-- DROP TABLE abastecimentos;

CREATE TABLE abastecimentos
(
  id serial NOT NULL,
  veiculo_id bigint NOT NULL,
  data date,
  nota_fiscal character varying(18),
  valor  numeric(10,2),
  litros integer,
  combustivel character varying(45),
  km numeric(10,0),
  obs character varying(500),
  CONSTRAINT abastecimentos_pkey PRIMARY KEY (id),
  CONSTRAINT abastecimentos_veiculo_id_fkey FOREIGN KEY (veiculo_id)
      REFERENCES veiculos (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE abastecimentos
  OWNER TO postgres;
