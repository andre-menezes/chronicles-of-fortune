# Validação de Dados e Respostas de Erro

## Objetivo deste documento

Registrar as regras de validação dos campos da API e o contrato de resposta de erro adotado pela plataforma.

## Quando usar

Use este documento ao implementar novos endpoints, tratar erros no frontend ou adicionar campos a DTOs existentes.

## Decisões que este documento fixa

- Toda validação de formato e tamanho ocorre na camada de DTO (entrada da API).
- Regras de negócio que envolvem múltiplos campos (ex: confirmação de senha) são validadas na camada de serviço.
- Respostas de erro seguem um contrato único, retornado pelo `GlobalExceptionHandler`.
- Mensagens de erro são sempre em inglês.

---

## Regras de validação — Cadastro de usuário

### `name`

| Regra | Detalhe |
|-------|---------|
| Obrigatório | Não pode ser vazio ou nulo |
| Tamanho | Mínimo 3 · Máximo 100 caracteres |
| Formato | Apenas letras (incluindo acentuadas) e espaços |
| Nome completo | Deve conter pelo menos nome e sobrenome (mínimo 2 palavras) |

Exemplo válido: `"Andre Menezes"` · Inválido: `"Andre"`, `"Andre123"`

---

### `username`

| Regra | Detalhe |
|-------|---------|
| Obrigatório | Não pode ser vazio ou nulo |
| Tamanho | Mínimo 3 · Máximo 50 caracteres |
| Formato | Apenas letras, números e `_` |
| Início | Deve começar com uma letra |
| Unicidade | Não pode estar em uso por outro usuário (verificado no banco) |

Exemplo válido: `"andre_menezes"` · Inválido: `"1andre"`, `"andre menezes"`, `"a"`

---

### `email`

| Regra | Detalhe |
|-------|---------|
| Obrigatório | Não pode ser vazio ou nulo |
| Formato | Deve ser um endereço de e-mail válido |
| Tamanho | Máximo 255 caracteres |
| Unicidade | Não pode estar em uso por outro usuário (verificado no banco) |

Exemplo válido: `"andre@email.com"` · Inválido: `"nao-e-email"`, `"@email.com"`

---

### `password`

| Regra | Detalhe |
|-------|---------|
| Obrigatório | Não pode ser vazio ou nulo |
| Tamanho | Mínimo 8 · Máximo 72 caracteres (limite do BCrypt) |
| Complexidade | Pelo menos 1 letra, 1 número e 1 caractere especial |
| Caracteres especiais aceitos | `! @ # $ % ^ & * ( ) _ + - = [ ] { } ; ' " \ | , . < > / ?` |

Exemplo válido: `"Senha@123"` · Inválido: `"senha123"` (sem especial), `"Senha@"` (curta demais)

---

### `confirmPassword`

| Regra | Detalhe |
|-------|---------|
| Obrigatório | Não pode ser vazio ou nulo |
| Igualdade | Deve ser idêntico ao campo `password` |

> A verificação de igualdade é feita na camada de serviço (`AuthService`), não via anotação de campo, pois envolve comparação entre dois campos do mesmo objeto.

---

## Contrato de resposta de erro

Todos os erros da API retornam o seguinte formato JSON:

```json
{
  "status": 400,
  "error": "Bad Request",
  "code": "VALIDATION_ERROR",
  "message": "Request contains invalid fields",
  "timestamp": "2026-03-23T20:00:00Z",
  "path": "/auth/register",
  "errors": [
    {
      "field": "name",
      "message": "Name must include first and last name and contain only letters",
      "rejectedValue": "Andre"
    }
  ]
}
```

### Campos

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `status` | `int` | Código HTTP numérico (`400`, `404`, `500`...) |
| `error` | `string` | Nome do status HTTP (`"Bad Request"`, `"Not Found"`...) |
| `code` | `string` | Código de erro da aplicação (ver tabela abaixo) |
| `message` | `string` | Descrição legível do erro, sempre em inglês |
| `timestamp` | `string` | Momento do erro em formato ISO-8601 UTC |
| `path` | `string` | Caminho da requisição que gerou o erro |
| `errors` | `array \| null` | Lista de erros por campo; presente apenas em `VALIDATION_ERROR` |

### Campos do objeto `errors`

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `field` | `string` | Nome do campo que falhou na validação |
| `message` | `string` | Descrição da regra violada |
| `rejectedValue` | `any` | Valor enviado que foi rejeitado |

---

## Códigos de erro da aplicação

| Código | Status HTTP | Quando ocorre |
|--------|-------------|---------------|
| `VALIDATION_ERROR` | 400 | Um ou mais campos do body violam regras de formato ou tamanho |
| `INVALID_REQUEST` | 400 | Regra de negócio violada (ex: senhas não conferem) |
| `USER_NOT_FOUND` | 404 | Usuário não encontrado pelo identificador informado |
| `INTERNAL_ERROR` | 500 | Erro inesperado no servidor |

---

## Decisões tomadas

- O campo `errors` é omitido (nulo) em erros que não sejam de validação de campos.
- `traceId` não entra no MVP — requer integração com rastreamento distribuído (OpenTelemetry). Pode ser adicionado sem quebrar o contrato.
- O frontend deve validar os mesmos campos por UX, mas o backend valida de forma independente pois a API pode ser consumida diretamente.

## Pontos para revisar depois

- Avaliar a adição de `traceId` quando o sistema evoluir para ambiente distribuído.
- Revisar política de senha conforme requisitos de segurança do produto amadurecerem.
- Definir validações para os demais endpoints (atualização de perfil, criação de Kingdom etc.) à medida que forem implementados.
