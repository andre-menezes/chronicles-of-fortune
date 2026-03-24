# Etapas de Desenvolvimento

## Objetivo deste documento

Traduzir a visão do produto e a arquitetura em um plano de execução técnico, com fases claras e entregáveis progressivos.

## Quando usar

Use este documento para orientar o desenvolvimento manual da plataforma, organizar a ordem de implementação e evitar construir sistemas prematuros.

## Decisões que este documento fixa

- O desenvolvimento será conduzido em fases incrementais.
- Cada fase deve entregar algo verificável antes da próxima.
- A complexidade será introduzida apenas quando houver base para sustentá-la.
- O produto será construído como plataforma full-stack desde o início do MVP.
- A experiência frontend será distribuída também como PWA.

## Fase 1: Base do Projeto e Documentação

Objetivo:

Montar a fundação conceitual e estrutural do projeto.

Entregáveis:

- documentação inicial atualizada
- definição da stack
- organização inicial de frontend, backend e banco
- alinhamento de módulos de domínio
- plano de conteúdo do MVP (`content-plan.md`)
- decisão de adoção de PWA no frontend

Pronto para avançar quando:

- a direção técnica estiver clara
- a documentação responder às perguntas principais do projeto

## Fase 2: Fundação do Backend e Banco

Objetivo:

Criar a base persistente da plataforma e preparar os módulos centrais.

Entregáveis:

- projeto Spring Boot inicial
- configuração de PostgreSQL
- entidades centrais mapeadas (`User`, `Kingdom`, `KingdomState`, `PlayerProgress`)
- persistência com JPA/Hibernate
- estrutura base de serviços, repositórios e controladores
- autenticação JWT completa
- engine de `NarrativeEvent` com choices, effects e trigger rules
- CRUD de `IncomeSource`
- CRUD de `ContextualTip` com seed inicial de 18 dicas
- `BehaviorLog` com rastreamento assíncrono via Spring Events

**Status: concluída.**

## Fase 3: Autenticação e Sessão

Objetivo:

Garantir acesso autenticado e base segura para operação do sistema.

Entregáveis:

- cadastro e login de `User`
- emissão e validação de JWT
- proteção de rotas e recursos principais
- integração inicial entre frontend e backend para sessão

Pronto para avançar quando:

- o usuário conseguir entrar no sistema de forma confiável
- o frontend conseguir consumir a API autenticada

## Fase 4: Frontend Nuxt e Base PWA

Objetivo:

Estruturar a experiência cliente como aplicação web instalável.

Entregáveis:

- frontend em Nuxt funcional
- configuração inicial de PWA
- manifesto da aplicação
- estratégia inicial de cache e atualização compatível com o MVP
- base para instalação da experiência no dispositivo

Pronto para avançar quando:

- a aplicação puder ser aberta e instalada como PWA
- o comportamento básico de atualização e acesso estiver claro

## Fase 5: Núcleo Jogável Integrado

Objetivo:

Implementar o loop principal do jogo com persistência real.

Entregáveis:

- carregamento do `Kingdom` autenticado
- ações básicas do jogador
- log narrativo
- processamento inicial de `NarrativeEvent`
- persistência do `KingdomState`
- exibição de `ContextualTip` por seção e após ações
- `IncomeSource` representada no reino
- experiência consistente no frontend e na PWA

Pronto para avançar quando:

- a experiência puder ser jogada de ponta a ponta
- as escolhas refletirem em dados persistidos

## Fase 6: CRUDs de Domínio e Conteúdo

Objetivo:

Permitir operação estruturada das entidades essenciais da plataforma.

Entregáveis:

- CRUD de `User`
- CRUD de `Kingdom` e dados relacionados
- CRUD de `NarrativeEvent` e conteúdo central
- CRUD de `IncomeSource`
- CRUD de `ContextualTip`
- base para `Quest`, `Chapter` e elementos administrativos futuros

Pronto para avançar quando:

- as principais entidades puderem ser mantidas com consistência
- a plataforma tiver base para crescer sem depender de dados fixos no código

## Fase 7: Expansão e Refinamento

Objetivo:

Aumentar a profundidade pedagógica e a maturidade técnica do produto.

Entregáveis:

- mais conteúdo e variedade de `NarrativeEvent`
- progressão mais rica via `PlayerProgress`
- refinamento de UX
- maior cobertura de testes
- observabilidade básica
- revisão do escopo offline e de cache da PWA

Pronto para avançar quando:

- houver clareza sobre os próximos experimentos de crescimento do produto

## Regras de execução

- Sempre construir o mínimo necessário para validar a próxima hipótese.
- Preferir slices verticais a grandes blocos de infraestrutura precoce.
- Documentar decisões importantes conforme o projeto evoluir.
- Revisar a arquitetura depois de uso real, não apenas por previsão.
- Manter backend e banco simples no suficiente para o MVP, mas corretos o bastante para crescer.
- Tratar PWA como mecanismo de acesso e retorno, não como promessa imediata de offline total.

## Decisões tomadas

- A execução será incremental e guiada por aprendizado.
- O MVP passa a depender de backend, autenticação e persistência.
- A documentação continua sendo parte da fundação do projeto, não um passo opcional.
- O frontend será distribuído também como PWA desde a base da experiência cliente.

## Pontos para revisar depois

- Adicionar checklists mais concretos por fase.
- Definir estratégia de testes por camada.
- Criar marcos de revisão de produto após cada fase importante.
- Refinar a estratégia de cache e atualização da PWA quando a implementação começar.
