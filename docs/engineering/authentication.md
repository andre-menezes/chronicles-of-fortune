# Autenticação

## Objetivo deste documento

Documentar a estratégia de autenticação da plataforma no MVP e registrar como o JWT participa do fluxo principal do sistema.

## Quando usar

Use este documento ao implementar login, proteção de rotas, sessão do usuário e integração autenticada entre frontend e backend.

## Decisões que este documento fixa

- A autenticação entrará no MVP.
- O backend usará JWT como mecanismo principal de autenticação.
- O foco inicial será access token e proteção básica de recursos.

## Estratégia de autenticação

O `User` se autentica com credenciais no backend. Uma vez validado, o sistema emite um JWT para representar a sessão autenticada. O frontend utiliza esse token para consumir recursos protegidos da API e acessar os dados do `Kingdom` e do `PlayerProgress`.

## Fluxo básico

1. O usuário envia `identifier` (email ou username) + senha para `POST /auth/login`.
2. O backend valida as credenciais.
3. O backend emite um JWT com validade de **24 horas**.
4. O frontend armazena o token e o envia no header `Authorization: Bearer <token>` em cada requisição autenticada.
5. O backend valida o token antes de permitir acesso ao recurso.

## Identificador de login

O campo `identifier` aceita **e-mail ou username** — ambos funcionam como identificador de acesso. A busca é feita por ambos os campos simultaneamente.

## Papel do access token

No MVP, o access token será suficiente para:

- identificar o `User` autenticado
- liberar acesso a recursos protegidos
- associar operações ao contexto da conta correta

Nesta rodada de documentação, não será detalhada uma estratégia avançada de refresh token, rotação ou revogação completa. Isso pode ser incorporado depois, se a evolução do produto exigir.

## Roles de usuário

Cada usuário possui um `role` que define seu nível de acesso:

| Role | Descrição | Atribuição |
|------|-----------|------------|
| `PLAYER` | Usuário padrão da plataforma | Atribuído automaticamente no cadastro |
| `ADMIN` | Acesso administrativo | Atribuído manualmente |

## Documentação interativa da API

O Swagger UI está disponível em:

- **Interface**: `http://localhost:8080/api/v1/swagger-ui/index.html`
- **API Docs (JSON)**: `http://localhost:8080/api/v1/v3/api-docs`

Para testar endpoints autenticados no Swagger: faça login, copie o token retornado e clique em **Authorize** (ícone de cadeado), inserindo apenas o token (sem o prefixo `Bearer`).

## Responsabilidades mínimas de segurança no MVP

- proteger endpoints autenticados
- validar credenciais com consistência
- evitar exposição indevida de dados entre usuários
- manter separação clara entre recursos públicos e privados
- garantir que cada usuário acesse apenas seus dados jogáveis e administrativos permitidos

## Relação com o frontend

O frontend deve tratar autenticação como parte do fluxo principal do produto. Isso significa:

- impedir acesso a telas autenticadas sem sessão válida
- carregar dados do `User` autenticado após login
- reagir corretamente à expiração ou invalidação de sessão

## Decisões tomadas

- JWT entra no MVP como mecanismo oficial de autenticação.
- A autenticação passa a fazer parte do fluxo principal do produto.
- O detalhamento profundo de refresh token fica fora desta rodada.

## Decisões tomadas

- Expiração do token definida em 24 horas (`expiration-ms: 86400000`).
- Login aceita e-mail ou username como identificador.
- Roles `PLAYER` e `ADMIN` implementadas; novos usuários recebem `PLAYER` por padrão.

## Pontos para revisar depois

- Refinar armazenamento seguro da sessão no frontend.
- Avaliar necessidade de refresh token conforme a maturidade do sistema.
- Definir política de promoção de usuário para `ADMIN` (endpoint ou processo manual).
