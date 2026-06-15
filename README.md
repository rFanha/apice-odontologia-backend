# Apice Odontologia — Backend

API REST em Java Spring Boot para gestao de usuarios, pacientes, dentistas, especialidades, consultas e leads da clinica Apice Odontologia.

---

## Requisitos

| Ferramenta | Versao minima |
|---|---|
| [Java JDK](https://jdk.java.net/26/) | 26 |
| [Maven](https://maven.apache.org/download.cgi) | 3.9 |
| [PostgreSQL Client (`psql`)](https://www.postgresql.org/download/) | Qualquer (apenas para rodar os scripts SQL) |

> O banco de dados ja esta hospedado remotamente. Nao e necessario instalar PostgreSQL Server localmente — apenas o cliente `psql` para executar os scripts de inicializacao.

---

## Passo a passo para rodar o projeto

### 1. Clonar o repositorio

```bash
git clone <url-do-repositorio>
cd apice-odontologia-backend
```

### 2. Criar o arquivo `.env`

Crie o arquivo `.env` na raiz do projeto com as seguintes variaveis:

```properties
DB_USERNAME=apiceodontologia
DB_PASSWORD=Apiceodontologia123
JWT_SECRET=troque-esta-chave-em-producao
JWT_EXPIRATION_MINUTES=120
```

> **Nota:** A aplicacao le esse arquivo automaticamente via `spring.config.import`. Voce tambem pode definir essas variaveis diretamente no sistema operacional em vez de usar o `.env`.

### 3. Inicializar o banco de dados

Execute os scripts SQL abaixo, **nessa ordem**. Eles criam as tabelas e inserem dados iniciais.

**No PowerShell (Windows):**

```powershell
$env:PGPASSWORD="Apiceodontologia123"
psql -h apiceodontologia.ddns.net -p 5432 -U apiceodontologia -d sistema_gestao_consultas -f src/main/resources/sql/01-ddl.sql
psql -h apiceodontologia.ddns.net -p 5432 -U apiceodontologia -d sistema_gestao_consultas -f src/main/resources/sql/02-dml.sql
```

**No Bash (Linux/Mac):**

```bash
PGPASSWORD="Apiceodontologia123" psql -h apiceodontologia.ddns.net -p 5432 -U apiceodontologia -d sistema_gestao_consultas -f src/main/resources/sql/01-ddl.sql
PGPASSWORD="Apiceodontologia123" psql -h apiceodontologia.ddns.net -p 5432 -U apiceodontologia -d sistema_gestao_consultas -f src/main/resources/sql/02-dml.sql
```

> Se o banco ja foi inicializado anteriormente, pule este passo.

### 4. Compilar o projeto

```bash
mvn clean install -DskipTests
```

### 5. Subir a API

```bash
mvn spring-boot:run
```

A API estara disponivel em:

```
http://localhost:8080
```

---

## Verificar se esta rodando

Faca uma requisicao de login para confirmar que a API esta no ar:

```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "admin@apiceodontologia.com.br",
  "senha": "Apice@123"
}
```

Resposta esperada: HTTP 200 com o token JWT.

---

## Usuarios iniciais (dados do DML)

| Email | Senha | Perfil |
|---|---|---|
| `admin@apiceodontologia.com.br` | `Apice@123` | Administrador |
| `marina.lopes@apiceodontologia.com.br` | `Apice@123` | Dentista |

---

## Autenticacao

Todas as rotas, exceto `POST /auth/login`, exigem JWT no header:

```http
Authorization: Bearer {{token}}
```

O token e retornado no corpo da resposta do login.

---

## Configuracao da aplicacao

| Parametro | Valor padrao |
|---|---|
| Porta | `8080` |
| Banco | `jdbc:postgresql://apiceodontologia.ddns.net:5432/sistema_gestao_consultas` |
| DDL auto | `none` (schema gerenciado pelos scripts SQL) |
| Expiracao JWT | `120` minutos (configuravel via `JWT_EXPIRATION_MINUTES`) |

---

## Documentacao

| Recurso | Caminho |
|---|---|
| Referencia da API (rotas e exemplos) | `docs/api-backend.md` |
| Roteiro de testes manuais | `docs/testes-backend.md` |
| Colecao Postman | `docs/postman/clinicawisestart-backend.postman_collection.json` |

---

## Comandos uteis

```bash
# Rodar testes
mvn test

# Compilar sem rodar
mvn clean package -DskipTests

# Verificar versao do Java
java -version

# Verificar versao do Maven
mvn -version
```
