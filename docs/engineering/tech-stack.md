# Stack Tecnológica

## Objetivo deste documento

Registrar as tecnologias escolhidas para a plataforma, o papel de cada uma e os principais tradeoffs envolvidos.

## Quando usar

Use este documento ao iniciar a implementação, revisar decisões técnicas ou justificar por que determinada tecnologia faz parte da base do projeto.

## Decisões que este documento fixa

- A plataforma será construída como sistema web full-stack.
- O frontend será baseado em Nuxt.
- O frontend adotará estratégia PWA.
- O backend será baseado em Java com Spring Boot.
- A autenticação do MVP usará JWT.
- A persistência usará JPA/Hibernate com PostgreSQL.

## Tecnologias principais

### Nuxt

Papel no produto:

- construir a aplicação web principal
- organizar páginas, layouts e fluxo de navegação
- integrar a interface com a API backend
- servir como base da experiência PWA

Justificativa:

- acelera a estrutura do frontend sobre o ecossistema Vue
- facilita organização de uma aplicação maior que um SPA simples
- cria uma base mais robusta para crescer o produto
- combina bem com uma experiência instalável e centrada em app

Tradeoffs:

- adiciona convenções próprias do framework
- exige disciplina na integração com estado, auth e consumo de API

### PWA

Papel no produto:

- aproximar a experiência web de um app instalável
- facilitar retorno do usuário à plataforma
- melhorar a percepção de continuidade e conveniência no uso recorrente

Justificativa:

- faz sentido para um produto de uso repetido e progressivo
- reduz fricção de acesso sem exigir app nativo no início
- reforça a proposta de plataforma educativa recorrente

Tradeoffs:

- requer cuidado com cache, atualização de versão e comportamento offline
- não deve gerar expectativa de suporte offline total antes de a arquitetura comportar isso

### TypeScript

Papel no produto:

- tipar contratos do frontend, DTOs e integrações com a API

Justificativa:

- reduz ambiguidade entre frontend e backend
- melhora manutenção e evolução do projeto

Tradeoffs:

- aumenta custo inicial de modelagem e alinhamento de contratos

### Java + Spring Boot

Papel no produto:

- expor a API da plataforma
- implementar autenticação, regras de domínio, CRUDs e persistência

Justificativa:

- ecossistema maduro para aplicações com autenticação, dados e operação administrativa
- boa separação em camadas
- forte base para crescimento de módulos de negócio

Tradeoffs:

- maior custo estrutural que um backend muito simples
- exige cuidado para evitar excesso de boilerplate e complexidade prematura

### JWT

Papel no produto:

- autenticar usuários e proteger acessos a recursos da plataforma

Justificativa:

- simples para o MVP
- adequado para separar autenticação e autorização básica em API stateless

Tradeoffs:

- requer cuidado com expiração, armazenamento no cliente e proteção de rotas
- não deve evoluir sem critério para cenários mais complexos de segurança

### JPA/Hibernate

Papel no produto:

- mapear entidades de domínio e persistência relacional

Justificativa:

- facilita a integração entre o domínio Java e o banco PostgreSQL
- ajuda a acelerar CRUDs e relações iniciais do sistema

Tradeoffs:

- exige cuidado com modelagem, consultas e carregamento de relacionamentos

### PostgreSQL

Papel no produto:

- armazenar `User`, `Kingdom`, `PlayerProgress`, conteúdo e demais dados da plataforma

Justificativa:

- banco relacional robusto
- adequado para entidades com vínculos claros e administração de conteúdo
- boa base para consistência e crescimento do sistema

Tradeoffs:

- exige modelagem relacional coerente desde cedo
- mudanças mal planejadas de schema podem gerar retrabalho

## Tecnologias complementares prováveis

### Pinia

- centralização de estado cliente no frontend, especialmente para sessão, `Kingdom` e UI

### Tailwind CSS

- aceleração da interface moderna e consistente do dashboard

### Spring Security

- apoio à autenticação e autorização no backend
- **Status: em uso** — filter chain JWT configurada, CSRF desabilitado, sessão stateless

### Liquibase

- controle de migrações de banco com versionamento incremental em YAML
- **Status: em uso** — migrations aplicadas via `db/changelog/db.changelog-master.yaml`

### BCrypt

- algoritmo de hash para senhas
- **Status: em uso** — implementado via `BCryptPasswordEncoder` do Spring Security
- Limite de 72 caracteres por restrição do algoritmo

### Vitest, JUnit e testes de integração

- cobertura de lógica no frontend e backend

## Critérios para adicionar novas tecnologias

- resolver uma necessidade concreta do produto
- reduzir complexidade em vez de aumentar a superfície de manutenção
- encaixar bem com a filosofia de evolução incremental do MVP

## Decisões tomadas

- Frontend alvo: Nuxt com estratégia PWA.
- Backend alvo: Java + Spring Boot.
- Autenticação entra no MVP com JWT.
- Persistência principal: JPA/Hibernate + PostgreSQL.

## Pontos para revisar depois

- Refinar o uso de Pinia dentro do frontend Nuxt.
- Definir o escopo real de offline, cache e atualização da PWA.
- Avaliar observabilidade, cache e infraestrutura depois do MVP integrado.
