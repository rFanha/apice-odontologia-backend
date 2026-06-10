# Apice Odontologia Backend

API REST em Java Spring Boot para gestao de usuarios, pacientes, dentistas, especialidades, consultas e relatorios da clinica Apice Odontologia.

## Requisitos

- Java 26
- Maven 3.9 ou superior
- PostgreSQL acessivel pela URL configurada em `src/main/resources/application.properties`
- Variaveis de ambiente ou arquivo `.env` com credenciais do banco

## Configuracao

Crie um arquivo `.env` na raiz do projeto ou defina as variaveis no sistema:

```properties
DB_USERNAME=usuario_do_banco
DB_PASSWORD=senha_do_banco
JWT_SECRET=troque-esta-chave-em-producao
JWT_EXPIRATION_MINUTES=120
```

A aplicacao usa por padrao:

- Porta: `8080`
- Banco: `jdbc:postgresql://apiceodontologia.ddns.net:5432/sistema_gestao_consultas`
- Schema controlado pelos scripts em `src/main/resources/sql`

## Banco De Dados

Execute os scripts abaixo no PostgreSQL, nessa ordem:

$env:PGPASSWORD="Apiceodontologia123"; psql -h apiceodontologia.ddns.net -p 5432 -U apiceodontologia -d sistema_gestao_consultas -f src/main/resources/sql/01-ddl.sql
$env:PGPASSWORD="Apiceodontologia123"; psql -h apiceodontologia.ddns.net -p 5432 -U apiceodontologia -d sistema_gestao_consultas -f src/main/resources/sql/02-dml.sql

```text
src/main/resources/sql/01-ddl.sql
src/main/resources/sql/02-dml.sql
```

O DML cria dados iniciais para usuarios, pacientes, dentistas, especialidades e uma consulta de exemplo.

Usuarios iniciais para testes:

```text
admin@apiceodontologia.com.br / Apice@123
marina.lopes@apiceodontologia.com.br / Apice@123
```

## Execucao

Para subir a API localmente:

```powershell
mvn spring-boot:run
```

URL base:

```text
http://localhost:8080
```

Para validar compilacao e contexto Spring:

```powershell
mvn test
```

## Documentacao Da API

- Rotas, exemplos de requisicao e resposta: `docs/api-backend.md`
- Roteiro de testes manuais: `docs/testes-backend.md`
- Colecao Postman: `docs/postman/clinicawisestart-backend.postman_collection.json`

## Autenticacao

Todas as rotas, exceto `POST /auth/login`, exigem JWT no header:

```http
Authorization: Bearer {{token}}
```

O token e retornado no login e pode ser usado no Postman pela variavel `token` da colecao.
