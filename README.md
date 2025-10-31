# crudOracle
docker pull container-registry.oracle.com/database/express:latest

´´´
docker run -d \
--name oracle-xe \
-p 1521:1521 \
-p 5500:5500 \
-e ORACLE_PWD=oracle \
-e ORACLE_CHARACTERSET=AL32UTF8 \
-v oracle-xe-data:/opt/oracle/oradata \
container-registry.oracle.com/database/express:latest
´´´

docker exec -it oracle-xe sqlplus system/oracle@XEPDB1

docker exec -it oracle-db-19c sqlplus system/oracle@ORCLPDB1

-- Crear el usuario HR
CREATE USER HR IDENTIFIED BY oracle;

-- Dar permisos al usuario HR
GRANT CONNECT, RESOURCE, DBA TO HR;
GRANT UNLIMITED TABLESPACE TO HR;

-- Verificar que se creó
SELECT username FROM dba_users WHERE username = 'HR';

-- Salir
EXIT;