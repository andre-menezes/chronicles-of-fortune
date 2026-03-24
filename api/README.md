# Chronicles of Fortune — Backend

API REST do projeto Chronicles of Fortune, construída com Java 21 e Spring Boot 4.

## Stack

| Tecnologia | Versão | Papel |
|---|---|---|
| Java | 21 | Linguagem |
| Spring Boot | 4.0.4 | Framework principal |
| Spring Security | — | Autenticação e proteção de rotas |
| Spring Data JPA / Hibernate | — | Persistência |
| PostgreSQL | 16 | Banco de dados |
| Liquibase | — | Migrações de banco |
| JJWT | 0.12.6 | Geração e validação de JWT |
| SpringDoc OpenAPI | 2.8.6 | Documentação da API |
| Lombok | — | Redução de boilerplate |

## Pré-requisitos

- Java 21+
- Maven 3.9+
- Docker e Docker Compose

## Rodando localmente

**1. Suba o banco de dados:**

```bash
docker compose up -d
```

Isso cria um container PostgreSQL com:

| Parâmetro | Valor |
|---|---|
| Host | `localhost:5432` |
| Database | `chronicles_db` |
| User | `chronicles_user` |
| Password | `chronicles_pass` |

**2. Configure a variável de ambiente do JWT:**

```bash
export JWT_SECRET=your-256-bit-secret-here
```

**3. Inicie a aplicação:**

```bash
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080/api/v1`.

## Documentação da API

Com a aplicação rodando, acesse:

- Swagger UI: `http://localhost:8080/api/v1/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api/v1/v3/api-docs`

## Endpoints

### Públicos

| Método | Path | Descrição |
|---|---|---|
| `POST` | `/api/v1/auth/register` | Cadastro de novo usuário |
| `POST` | `/api/v1/auth/login` | Login (retorna JWT) |

### Autenticados

Requerem o header `Authorization: Bearer <token>`.

| Método | Path | Descrição |
|---|---|---|
| `GET` | `/api/v1/users/me` | Dados do usuário autenticado |

Consulte `docs/engineering/api-endpoints.md` para o contrato completo de cada endpoint.

## Migrações de banco

As migrações ficam em `src/main/resources/db/changelog/migrations/` e são aplicadas automaticamente pelo Liquibase ao iniciar a aplicação.

| Migration | Descrição |
|---|---|
| `001-create-users.yaml` | Tabela de usuários |
| `002-create-kingdoms.yaml` | Tabela de reinos |
| `003-create-kingdom-states.yaml` | Tabela de estados do reino |
| `004-create-player-progress.yaml` | Tabela de progresso do jogador |
| `005-add-name-to-users.yaml` | Campo `name` na tabela de usuários |

## Estrutura de pacotes

```
com.om.chroniclesoffortune.backend
├── domain
│   ├── auth          # Autenticação, JWT, filtros
│   └── user          # Entidade User, controller, repositório
└── shared
    ├── exception     # Tratamento global de erros
    ├── security      # Configuração do Spring Security
    └── OpenApiConfig # Configuração do Swagger
```

## Testes

```bash
./mvnw test
```

Os testes usam H2 em memória (perfil `test`), configurado em `src/test/resources/application-test.yml`.

## Perfis

| Perfil | Quando usar |
|---|---|
| `dev` (padrão) | Desenvolvimento local com PostgreSQL via Docker |
| `test` | Testes automatizados com H2 em memória |
