# API Endpoints

## Objetivo deste documento

Registrar o contrato de todos os endpoints da API: paths, métodos, request/response e comportamentos de erro.

## Quando usar

Use este documento ao integrar o frontend com a API, escrever testes ou implementar novos endpoints.

## Decisões que este documento fixa

- Todos os endpoints usam o prefixo `/api/v1` (configurado via `spring.mvc.servlet.path`).
- Endpoints autenticados exigem o header `Authorization: Bearer <token>`.
- Respostas de erro seguem o contrato definido em `api-validation.md`.

---

## Endpoints públicos

### `POST /api/v1/auth/register`

Cadastra um novo usuário na plataforma.

**Request body:**
```json
{
  "name": "Andre Menezes",
  "username": "andre_menezes",
  "email": "andre@email.com",
  "password": "Senha@123",
  "confirmPassword": "Senha@123"
}
```

**Response `200 OK`:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Erros possíveis:**

| Status | Code | Quando |
|--------|------|--------|
| 400 | `VALIDATION_ERROR` | Campos inválidos |
| 400 | `INVALID_REQUEST` | `password` e `confirmPassword` não conferem |

---

### `POST /api/v1/auth/login`

Autentica um usuário existente.

**Request body:**
```json
{
  "identifier": "andre_menezes",
  "password": "Senha@123"
}
```

> O campo `identifier` aceita **e-mail ou username**.

**Response `200 OK`:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Erros possíveis:**

| Status | Code | Quando |
|--------|------|--------|
| 401 | — | Credenciais inválidas |
| 404 | `USER_NOT_FOUND` | Usuário não encontrado |

---

## Endpoints autenticados (PLAYER)

Todos requerem o header:
```
Authorization: Bearer <token>
```

---

### `GET /api/v1/users/me`

Retorna os dados do usuário autenticado.

**Response `200 OK`:**
```json
{
  "name": "Andre Menezes",
  "username": "andre_menezes",
  "email": "andre@email.com",
  "role": "PLAYER"
}
```

---

### `POST /api/v1/kingdoms`

Cria o reino do usuário autenticado. Cada usuário pode ter apenas um reino.

**Request body:**
```json
{
  "name": "Reino das Finanças"
}
```

**Response `200 OK`:**
```json
{
  "kingdom": {
    "id": "uuid",
    "name": "Reino das Finanças",
    "createdAt": "2026-03-24T12:00:00"
  },
  "state": {
    "gold": 0,
    "mana": 0,
    "resilience": 0,
    "stability": 100.00
  }
}
```

**Erros possíveis:**

| Status | Code | Quando |
|--------|------|--------|
| 400 | `VALIDATION_ERROR` | Nome inválido (menos de 3 ou mais de 100 chars) |
| 409 | `KINGDOM_ALREADY_EXISTS` | Usuário já possui um reino |

---

### `GET /api/v1/kingdoms/me`

Retorna o reino e o estado atual do usuário autenticado.

**Response `200 OK`:** mesmo formato de `POST /kingdoms`.

**Erros possíveis:**

| Status | Code | Quando |
|--------|------|--------|
| 404 | `KINGDOM_NOT_FOUND` | Usuário ainda não criou seu reino |

---

### `GET /api/v1/narrative-events/next`

Retorna o próximo evento narrativo aplicável ao usuário, baseado em nível, regras de disparo e eventos já resolvidos.

**Response `200 OK`:**
```json
{
  "id": "uuid",
  "title": "O Início do Ciclo",
  "description": "Os recursos do reino chegam..."
}
```

**Response `204 No Content`:** nenhum evento aplicável no momento.

---

### `GET /api/v1/narrative-events/{id}`

Retorna os detalhes de um evento com suas escolhas disponíveis.

**Response `200 OK`:**
```json
{
  "id": "uuid",
  "title": "O Início do Ciclo",
  "description": "...",
  "choices": [
    {
      "id": "uuid",
      "text": "Guardar na reserva",
      "description": null,
      "displayOrder": 1
    }
  ]
}
```

**Erros possíveis:**

| Status | Code | Quando |
|--------|------|--------|
| 404 | `EVENT_NOT_FOUND` | Evento não encontrado |

---

### `POST /api/v1/narrative-events/{id}/choices/{choiceId}/resolve`

Resolve uma escolha de evento narrativo, aplicando os efeitos ao `KingdomState` e ao `PlayerProgress`.

**Response `200 OK`:**
```json
{
  "state": {
    "gold": 10.00,
    "mana": 0,
    "resilience": 0,
    "stability": 100.00
  },
  "progress": {
    "level": 1,
    "experiencePoints": 0
  }
}
```

**Erros possíveis:**

| Status | Code | Quando |
|--------|------|--------|
| 404 | `EVENT_NOT_FOUND` | Evento não encontrado |
| 404 | `CHOICE_NOT_FOUND` | Escolha não pertence ao evento |
| 409 | `EVENT_ALREADY_RESOLVED` | Usuário já resolveu este evento |

---

### `POST /api/v1/income-sources`

Cria uma nova fonte de renda para o usuário autenticado.

**Request body:**
```json
{
  "name": "Salário Empresa",
  "type": "SALARY",
  "amount": 5000.00
}
```

> Tipos válidos: `SALARY`, `RENTAL`, `FREELANCE`, `DIVIDENDS`, `OTHER`

**Response `200 OK`:**
```json
{
  "id": "uuid",
  "name": "Salário Empresa",
  "type": "SALARY",
  "amount": 5000.00,
  "active": true,
  "createdAt": "2026-03-24T12:00:00"
}
```

**Erros possíveis:**

| Status | Code | Quando |
|--------|------|--------|
| 400 | `VALIDATION_ERROR` | Nome inválido (< 3 ou > 100 chars) ou amount < 0.01 |
| 404 | `KINGDOM_NOT_FOUND` | Usuário ainda não criou seu reino |

---

### `GET /api/v1/income-sources`

Lista todas as fontes de renda do usuário autenticado, ordenadas por `createdAt` decrescente.

**Response `200 OK`:** array de `IncomeSourceResponse`.

---

### `GET /api/v1/income-sources/{id}`

Retorna uma fonte de renda específica do usuário.

**Erros possíveis:**

| Status | Code | Quando |
|--------|------|--------|
| 404 | `INCOME_SOURCE_NOT_FOUND` | Não encontrado ou pertence a outro usuário |

---

### `PUT /api/v1/income-sources/{id}`

Atualiza uma fonte de renda. Todos os campos são opcionais — campos `null` não são alterados.

**Request body:**
```json
{
  "name": "Salário Novo",
  "type": "FREELANCE",
  "amount": 6000.00,
  "active": false
}
```

**Response `200 OK`:** `IncomeSourceResponse` atualizado.

**Erros possíveis:**

| Status | Code | Quando |
|--------|------|--------|
| 404 | `INCOME_SOURCE_NOT_FOUND` | Não encontrado ou pertence a outro usuário |

---

### `DELETE /api/v1/income-sources/{id}`

Remove uma fonte de renda do usuário.

**Response `204 No Content`.**

**Erros possíveis:**

| Status | Code | Quando |
|--------|------|--------|
| 404 | `INCOME_SOURCE_NOT_FOUND` | Não encontrado ou pertence a outro usuário |

---

### `GET /api/v1/contextual-tips?context={context}`

Retorna dicas financeiras ativas para o contexto solicitado.

> Contextos válidos: `DASHBOARD`, `INCOME_SOURCE`, `EMERGENCY_RESERVE`, `DEBT`, `BUDGET`, `POST_ACTION`

**Response `200 OK`:**
```json
[
  {
    "id": "uuid",
    "text": "Reserva de emergência não é luxo — é proteção.",
    "context": "DASHBOARD",
    "category": "EMERGENCY_RESERVE",
    "active": true,
    "createdAt": "2026-03-24T12:00:00"
  }
]
```

---

## Endpoints administrativos (ADMIN)

Requerem role `ADMIN`. Retornam `403 Forbidden` para qualquer outro perfil.

---

### `POST /api/v1/admin/narrative-events`

Cria um novo evento narrativo com escolhas, efeitos e regras de disparo.

---

### `PUT /api/v1/admin/narrative-events/{id}`

Atualiza um evento narrativo existente.

---

### `DELETE /api/v1/admin/narrative-events/{id}`

Remove um evento narrativo.

**Response `204 No Content`.**

---

### `POST /api/v1/admin/contextual-tips`

Cria uma nova dica contextual.

**Request body:**
```json
{
  "text": "Reserva de emergência não é luxo — é proteção.",
  "context": "DASHBOARD",
  "category": "EMERGENCY_RESERVE"
}
```

> Categorias válidas: `CASH_FLOW`, `EMERGENCY_RESERVE`, `DEBT`, `INCOME_SOURCES`, `CONSCIOUS_SPENDING`, `INTEREST`, `PLANNING`

**Erros possíveis:**

| Status | Code | Quando |
|--------|------|--------|
| 400 | `VALIDATION_ERROR` | Texto com menos de 10 ou mais de 500 chars |

---

### `GET /api/v1/admin/contextual-tips`

Lista todas as dicas contextuais (ativas e inativas).

---

### `PUT /api/v1/admin/contextual-tips/{id}`

Atualiza uma dica contextual. Todos os campos são opcionais.

**Erros possíveis:**

| Status | Code | Quando |
|--------|------|--------|
| 404 | `CONTEXTUAL_TIP_NOT_FOUND` | Dica não encontrada |

---

### `DELETE /api/v1/admin/contextual-tips/{id}`

Remove uma dica contextual.

**Response `204 No Content`.**

**Erros possíveis:**

| Status | Code | Quando |
|--------|------|--------|
| 404 | `CONTEXTUAL_TIP_NOT_FOUND` | Dica não encontrada |

---

### `GET /api/v1/admin/behavior-logs`

Lista os logs de comportamento do usuário com filtros opcionais.

**Query params:**

| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| `userId` | UUID | não | Filtra por usuário |
| `action` | String | não | Filtra por tipo de ação |

> Ações válidas: `KINGDOM_CREATED`, `NARRATIVE_CHOICE_RESOLVED`, `INCOME_SOURCE_CREATED`, `INCOME_SOURCE_UPDATED`, `INCOME_SOURCE_DELETED`, `TIPS_VIEWED`

**Response `200 OK`:**
```json
[
  {
    "id": "uuid",
    "userId": "uuid",
    "action": "INCOME_SOURCE_CREATED",
    "metadata": "{\"type\":\"SALARY\"}",
    "createdAt": "2026-03-24T12:00:00"
  }
]
```

---

## Convenções gerais

- **Content-Type**: `application/json` em todas as requisições com body.
- **Token**: JWT com validade de 24 horas. Após expirar, é necessário fazer login novamente.
- **Role no response**: retornado sem o prefixo `ROLE_` (ex: `"PLAYER"`, não `"ROLE_PLAYER"`).
- **Isolamento de dados**: endpoints de PLAYER retornam apenas dados do próprio usuário. Tentar acessar recurso de outro usuário retorna `404`, não `403`.
