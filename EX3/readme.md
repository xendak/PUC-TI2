# EX3 - Integração PostgreSQL e Spark


## Instruçoes
assumindo instruçoes utilizando o flake.nix e direnv (usuario "drops")
1. executar o pgstart.sh para inicializar o database.
    - criar um novo database usando `psql --host=localhost -c 'create database ti2'`
    - ou usar o padrao postgres e mudar no arquivo dao.java e na linha abaixo --dbname=postgres
2. executar `psql --host=localhost --dbname=ti2`
3. criar tabela no pqsl:
```sql
CREATE DATABASE ti2;
\c ti2
CREATE TABLE produtos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10,2) NOT NULL
);

4. compilar utilizando o maven ou apenas o javac
    - `mvn clean package`
5. rodar o programa e testar.
    - `java -jar ./target/ex2-1.0.jar`
