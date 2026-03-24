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
| 400 | `VALIDATION_ERROR` | Campos inválidos (ver regras em `api-validation.md`) |
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
| 401 | — | Credenciais inválidas (tratado pelo Spring Security) |
| 404 | `USER_NOT_FOUND` | Usuário não encontrado pelo `identifier` |

---

## Endpoints autenticados

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

**Erros possíveis:**

| Status | Code | Quando |
|--------|------|--------|
| 401 | — | Token ausente ou inválido |
| 403 | — | Token expirado |

---

### `POST /api/v1/kingdoms`

Cria o reino do usuário autenticado. Cada usuário pode ter apenas um reino.

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

**Request body:**
```json
{
  "name": "Reino das Finanças"
}
```

**Erros possíveis:**

| Status | Code | Quando |
|--------|------|--------|
| 400 | `VALIDATION_ERROR` | Nome inválido (menos de 3 ou mais de 100 chars, ou em branco) |
| 401 | — | Token ausente ou inválido |
| 409 | `KINGDOM_ALREADY_EXISTS` | Usuário já possui um reino |

---

### `GET /api/v1/kingdoms/me`

Retorna o reino e o estado atual do usuário autenticado.

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
| 401 | — | Token ausente ou inválido |
| 404 | `KINGDOM_NOT_FOUND` | Usuário ainda não criou seu reino |

---

## Convenções gerais

- **Content-Type**: `application/json` em todas as requisições com body.
- **Token**: JWT com validade de 24 horas. Após expirar, é necessário fazer login novamente.
- **Role no response**: retornado sem o prefixo `ROLE_` (ex: `"PLAYER"`, não `"ROLE_PLAYER"`).
