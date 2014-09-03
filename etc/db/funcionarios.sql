-- Table: usuarios

-- DROP TABLE usuarios;

CREATE TABLE usuarios
(
  id serial NOT NULL,
  usuario character varying(16) NOT NULL,
  nome character varying(45),
  CONSTRAINT pk_usuario PRIMARY KEY (id),
  CONSTRAINT unique_usuario UNIQUE (usuario)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE usuarios
  OWNER TO postgres;
