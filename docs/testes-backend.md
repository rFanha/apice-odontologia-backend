# Testes Manuais Do Back-End

Este roteiro cobre os endpoints REST usando Postman ou Insomnia.

## Preparacao

1. Subir a API com `mvn spring-boot:run`.
2. Confirmar URL base `http://localhost:8080`.
3. Importar a colecao `docs/postman/clinicawisestart-backend.postman_collection.json` no Postman.
4. Configurar a variavel `baseUrl` como `http://localhost:8080`.
5. Executar `POST /auth/login` e conferir se a variavel `token` foi preenchida.

## Checklist De Execucao

| Grupo | Requisicoes | Resultado esperado |
| --- | --- | --- |
| Auth | Login valido e login invalido | Token no login valido; erro padronizado no login invalido |
| Usuarios | Listar, buscar, criar, atualizar e excluir | `200`, `201` ou `204`; exige perfil `ADMIN` |
| Pacientes | Listar, buscar, criar, atualizar e excluir | CRUD funcional com validacao de obrigatorios, tamanho e email |
| Dentistas | Listar, filtrar por ativo, buscar, criar, atualizar e excluir | CRUD funcional com validacao de CPF, email, CRO e ativo |
| Especialidades | Listar, buscar, criar, atualizar e excluir | CRUD funcional com validacao de nome obrigatorio |
| Consultas | Listar, buscar, criar, atualizar, cancelar e excluir | Valida paciente, dentista, periodo futuro, conflito de horario e cancelamento |
| Relatorios | Dashboard sem filtros e com filtros | Retorna totais coerentes com os filtros |
| Seguranca | Chamar rotas sem token e com perfil sem permissao | `401 Unauthorized` ou `403 Forbidden` |
| Erros | Enviar JSON invalido ou campos invalidos | Resposta no formato `ErroResponse` |

## Casos De Validacao

Use os payloads abaixo para confirmar validacoes.

Email invalido:

```json
{
  "nome": "Paciente Teste",
  "email": "email-invalido",
  "cpf": "99999999999",
  "telefone": "(11) 95555-4444"
}
```

Consulta com periodo invalido:

```json
{
  "pacienteId": 1,
  "dentistaId": 1,
  "descricao": "Periodo invalido",
  "dataInicio": "2026-06-15T10:00:00",
  "dataFim": "2026-06-15T09:00:00",
  "status": "AGENDADA"
}
```

Consulta no passado:

```json
{
  "pacienteId": 1,
  "dentistaId": 1,
  "descricao": "Data passada",
  "dataInicio": "2020-01-01T09:00:00",
  "dataFim": "2020-01-01T10:00:00",
  "status": "AGENDADA"
}
```

## Evidencia Esperada

Ao finalizar os testes, salve no Postman ou Insomnia:

- A colecao executada.
- O ambiente com `baseUrl` e `token`.
- Prints ou exportacao das respostas principais, se necessario para apresentacao.
