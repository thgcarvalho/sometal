-- Table: contas

-- DROP TABLE veiculos;

CREATE TABLE veiculos
(
  id serial NOT NULL,
  placa character varying(8) NOT NULL,
  chassi character varying(45),
  renavam character varying(45),
  marca character varying(45),
  modelo character varying(45),
  cor character varying(45),
  combustivel character varying(45),
  anofab integer,
  anomod integer,
  obs character varying(500),
  CONSTRAINT veiculos_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE veiculos
  OWNER TO postgres;
