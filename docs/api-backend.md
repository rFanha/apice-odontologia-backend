# API Back-End

URL base local:

```text
http://localhost:8080
```

Todas as rotas, exceto `POST /auth/login`, exigem:

```http
Authorization: Bearer {{token}}
Content-Type: application/json
```

## Autenticacao

### POST /auth/login

Request:

```json
{
  "email": "admin@apiceodontologia.com.br",
  "senha": "sua-senha"
}
```

Response `200 OK`:

```json
{
  "id": 1,
  "nome": "Administrador Apice",
  "email": "admin@apiceodontologia.com.br",
  "perfil": "ADMIN",
  "ativo": true,
  "token": "jwt-gerado-pela-api"
}
```

## Usuarios

Rotas restritas a perfil `ADMIN`.

| Metodo | Rota | Descricao |
| --- | --- | --- |
| GET | `/usuarios` | Lista usuarios |
| GET | `/usuarios/{id}` | Busca usuario por id |
| POST | `/usuarios` | Cria usuario |
| PUT | `/usuarios/{id}` | Atualiza usuario |
| DELETE | `/usuarios/{id}` | Exclui usuario |

Request `POST /usuarios`:

```json
{
  "nome": "Dra. Julia Ramos",
  "cpf": "66666666666",
  "email": "julia.ramos@apiceodontologia.com.br",
  "senha": "senha-segura",
  "perfil": "DENTISTA",
  "ativo": true
}
```

Response `201 Created`:

```json
{
  "id": 3,
  "nome": "Dra. Julia Ramos",
  "cpf": "66666666666",
  "email": "julia.ramos@apiceodontologia.com.br",
  "perfil": "DENTISTA",
  "ativo": true,
  "dataCriacao": "2026-06-10T12:00:00",
  "ultimoLogin": null
}
```

## Pacientes

| Metodo | Rota | Descricao |
| --- | --- | --- |
| GET | `/pacientes` | Lista pacientes |
| GET | `/pacientes/{id}` | Busca paciente por id |
| POST | `/pacientes` | Cria paciente |
| PUT | `/pacientes/{id}` | Atualiza paciente |
| DELETE | `/pacientes/{id}` | Exclui paciente |

Request `POST /pacientes`:

```json
{
  "nome": "Beatriz Martins",
  "email": "beatriz.martins@email.com",
  "cpf": "77777777777",
  "telefone": "(11) 96666-3333"
}
```

Response `201 Created`:

```json
{
  "id": 3,
  "nome": "Beatriz Martins",
  "email": "beatriz.martins@email.com",
  "cpf": "77777777777",
  "telefone": "(11) 96666-3333",
  "dataCriacao": "2026-06-10T12:00:00"
}
```

## Dentistas

| Metodo | Rota | Descricao |
| --- | --- | --- |
| GET | `/dentistas` | Lista dentistas |
| GET | `/dentistas?ativo=true` | Lista dentistas filtrando por ativo |
| GET | `/dentistas/{id}` | Busca dentista por id |
| POST | `/dentistas` | Cria dentista |
| PUT | `/dentistas/{id}` | Atualiza dentista |
| DELETE | `/dentistas/{id}` | Exclui dentista |

Request `POST /dentistas`:

```json
{
  "nome": "Dra. Laura Nunes",
  "cpf": "88888888888",
  "email": "laura.nunes@apiceodontologia.com.br",
  "cro": "CRO-SP-24680",
  "ativo": true
}
```

Response `201 Created`:

```json
{
  "id": 3,
  "nome": "Dra. Laura Nunes",
  "cpf": "88888888888",
  "email": "laura.nunes@apiceodontologia.com.br",
  "cro": "CRO-SP-24680",
  "ativo": true,
  "dataCriacao": "2026-06-10T12:00:00"
}
```

## Especialidades

| Metodo | Rota | Descricao |
| --- | --- | --- |
| GET | `/especialidades` | Lista especialidades |
| GET | `/especialidades/{id}` | Busca especialidade por id |
| POST | `/especialidades` | Cria especialidade |
| PUT | `/especialidades/{id}` | Atualiza especialidade |
| DELETE | `/especialidades/{id}` | Exclui especialidade |

Request `POST /especialidades`:

```json
{
  "nome": "Implantodontia"
}
```

Response `201 Created`:

```json
{
  "id": 3,
  "nome": "Implantodontia"
}
```

## Consultas

Rotas restritas a perfis `ADMIN` e `DENTISTA`.

| Metodo | Rota | Descricao |
| --- | --- | --- |
| GET | `/consultas` | Lista consultas |
| GET | `/consultas?dentistaId=1` | Lista consultas por dentista |
| GET | `/consultas/{id}` | Busca consulta por id |
| POST | `/consultas` | Cria consulta |
| PUT | `/consultas/{id}` | Atualiza consulta |
| PUT | `/consultas/{id}/cancelar` | Cancela consulta |
| DELETE | `/consultas/{id}` | Exclui consulta |

Request `POST /consultas`:

```json
{
  "pacienteId": 1,
  "dentistaId": 1,
  "descricao": "Avaliacao de rotina",
  "motivoCancelamento": null,
  "dataInicio": "2026-06-15T09:00:00",
  "dataFim": "2026-06-15T10:00:00",
  "status": "AGENDADA"
}
```

Response `201 Created`:

```json
{
  "id": 2,
  "pacienteId": 1,
  "dentistaId": 1,
  "usuarioId": 1,
  "descricao": "Avaliacao de rotina",
  "motivoCancelamento": null,
  "dataInicio": "2026-06-15T09:00:00",
  "dataFim": "2026-06-15T10:00:00",
  "dataRegistro": "2026-06-10T12:00:00",
  "status": "AGENDADA"
}
```

Request `PUT /consultas/{id}/cancelar`:

```json
{
  "motivoCancelamento": "Paciente solicitou remarcacao."
}
```

## Relatorios

Rotas restritas a perfil `ADMIN`.

| Metodo | Rota | Descricao |
| --- | --- | --- |
| GET | `/relatorios/dashboard` | Retorna indicadores gerais |
| GET | `/relatorios/dashboard?usuarioId=1&pacienteId=1&especialidadeId=1&dataInicio=2026-06-01T00:00:00&dataFim=2026-06-30T23:59:59` | Retorna indicadores filtrados |

Response `200 OK`:

```json
{
  "totalConsultas": 10,
  "consultasAgendadas": 7,
  "consultasCanceladas": 1,
  "consultasFinalizadas": 2,
  "totalPacientes": 2,
  "totalDentistas": 2,
  "totalEspecialidades": 2
}
```

## Erros

Erros seguem o formato padronizado:

```json
{
  "timestamp": "2026-06-10T12:00:00",
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Dados invalidos.",
  "caminho": "/pacientes",
  "campos": {
    "email": "Email deve ter um formato valido."
  }
}
```
