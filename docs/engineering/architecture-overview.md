# Visão Arquitetural

## Objetivo deste documento

Descrever a arquitetura de alto nível da plataforma e como seus principais módulos se conectam.

## Quando usar

Use este documento para orientar a implementação, revisar limites entre camadas e garantir que o sistema cresça de forma organizada.

## Decisões que este documento fixa

- A plataforma será organizada como sistema cliente-servidor.
- Frontend, backend, autenticação, domínio e persistência devem permanecer separados por responsabilidade.
- O frontend será entregue com estratégia PWA.
- O projeto deve favorecer escalabilidade sem perder simplicidade no MVP.

## Visão geral do sistema

A plataforma pode ser entendida em seis blocos principais:

- frontend web em Nuxt
- camada PWA
- camada de autenticação
- API backend
- domínio e regras narrativas
- persistência e banco de dados

O frontend entrega a experiência do usuário. A camada PWA adiciona instalação, manifesto e comportamento controlado de cache. A camada de autenticação controla acesso e sessão. A API backend expõe operações do sistema. O domínio concentra regras do `Kingdom`, do `PlayerProgress` e do conteúdo. A persistência mantém os dados estruturados da plataforma.

## Módulos principais

### Frontend web

Responsável por apresentar:

- autenticação e entrada do usuário
- painel do `Kingdom`
- central de log narrativo
- ações disponíveis
- feedback visual de progresso
- `ContextualTip` por seção

### Camada PWA

Responsável por:

- permitir instalação da aplicação
- registrar manifesto e identidade de app
- controlar estratégia inicial de cache e atualização
- melhorar o retorno do usuário à plataforma

### Autenticação

Responsável por:

- login e identificação do `User`
- emissão e validação de JWT
- proteção básica de rotas e recursos

### API backend

Responsável por:

- receber requisições do frontend
- expor operações de leitura e escrita
- orquestrar autenticação, domínio e persistência

### Domínio e regras narrativas

Responsável por:

- estado do `Kingdom` e `KingdomState`
- regras de progressão
- processamento de ações e `NarrativeEvent`
- integridade do comportamento principal da plataforma

### Persistência e banco

Responsável por:

- armazenar entidades de `User`
- armazenar `Kingdom` e `PlayerProgress`
- armazenar `IncomeSource`, `ContextualTip` e `BehaviorLog`
- armazenar conteúdo e estruturas administrativas
- suportar CRUDs e consulta de dados

## Fluxo principal

1. O usuário abre a aplicação web ou instalada como PWA.
2. O usuário se autentica no frontend.
3. O frontend envia credenciais ao backend.
4. O backend valida o `User` e retorna JWT.
5. O frontend consome a API autenticada.
6. O backend recupera ou atualiza dados no banco.
7. O domínio processa regras e devolve o estado atualizado.
8. O frontend renderiza o novo estado e o feedback narrativo.

## Princípios arquiteturais

- A interface não deve conter regras centrais de negócio.
- A API deve ser a porta oficial de acesso aos dados persistidos.
- Regras de domínio devem ficar no backend quando influenciarem estado persistente.
- A PWA deve melhorar acesso e continuidade, não introduzir offline complexo antes da hora.
- Conteúdo narrativo e entidades administrativas devem ser modelados para CRUD sem contaminar a UI com detalhes de persistência.
- O MVP deve ter fundação suficiente para crescer sem exigir reescrita completa da arquitetura.

## Estrutura de referência

```text
frontend/
  app Nuxt + configuração PWA
backend/
  app Spring Boot
infra/
  banco e configurações futuras
docs/
  produto e engenharia
```

Essa estrutura é apenas uma referência inicial. O critério principal é manter clareza entre experiência do usuário, API, domínio e dados.

## Decisões tomadas

- A plataforma deixa de ser documentada como frontend-only.
- O backend passa a ser parte estrutural do MVP.
- Banco relacional e autenticação passam a fazer parte da arquitetura-base.
- O frontend adotará distribuição em formato PWA.

## Pontos para revisar depois

- Detalhar contratos entre frontend e backend.
- Definir critérios de autorização por perfil quando a administração crescer.
- Refinar a distribuição entre regras de frontend e regras de backend conforme o domínio evoluir.
- Definir o escopo real de cache e offline da PWA.
