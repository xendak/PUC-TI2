# EX2 - Integração PostgreSQL

## Instruçoes
assumindo instruçoes utilizando o flake.nix e direnv (usuario "drops")
1. executar o pgstart.sh para inicializar o database.
    - criar um novo database usando `psql --host=localhost -c 'create database ti2'`
    - ou usar o padrao postgres e mudar no arquivo dao.java e na linha abaixo --dbname=postgres
2. executar `psql --host=localhost --dbname=ti2`
3. criar tabela do sql
```
CREATE TABLE Pessoa (
 id SERIAL PRIMARY KEY,
 idade INT,
 nome TEXT NOT NULL,
 profissao TEXT
)
```
4. compilar utilizando o maven ou apenas o javac
    - `mvn clean package`
5. rodar o programa e testar.
    - `java -jar ./target/ex2-1.0.jar`
