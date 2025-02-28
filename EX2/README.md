# EX2 - Integração PostgreSQL

## Instruçoes
assumindo instruçoes utilizando o flake.nix e direnv
1. executar o pgstart.sh para inicializar o database.
2. executar `psql --host=localhost --dbname=postgres`
3. criar tabela do sql

```
CREATE TABLE Pessoa (
 id SERIAL PRIMARY KEY,
 idade INT,
 nome VARCHAR(100) NOT NULL,
 profissao VARCHAR(100) not NULL
)
```
4. compilar utilizando o maven ou apenas o javac
    - `mvn clean package`
    -
5. rodar o programa e testar.
