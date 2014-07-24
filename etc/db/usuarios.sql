-- Table: usuarios

-- DROP TABLE usuarios;

CREATE TABLE usuarios
(
  usuario character varying(16) NOT NULL,
  nome character varying(45),
  funcao character varying(45),
  email character varying(45),
  CONSTRAINT pk_usuario PRIMARY KEY (usuario)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE usuarios
  OWNER TO grandevc;
GRANT ALL ON TABLE usuarios TO grandevc;