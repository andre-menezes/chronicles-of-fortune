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

## Convenções gerais

- **Content-Type**: `application/json` em todas as requisições com body.
- **Token**: JWT com validade de 24 horas. Após expirar, é necessário fazer login novamente.
- **Role no response**: retornado sem o prefixo `ROLE_` (ex: `"PLAYER"`, não `"ROLE_PLAYER"`).
