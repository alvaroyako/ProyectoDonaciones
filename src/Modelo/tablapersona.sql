DROP TABLE PERSONAS;

CREATE TABLE PERSONAS ( Nombre VARCHAR2(50),
    Apellido VARCHAR2(50),
    Email VARCHAR2(50) PRIMARY KEY,
    Sexo VARCHAR2(1),
    Casado VARCHAR2(1)
);