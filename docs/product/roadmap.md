# Roadmap de Produto

## Objetivo deste documento

Organizar a evolução da plataforma em marcos progressivos, conectando a visão de produto a entregas realistas.

## Quando usar

Use este documento para orientar priorização, sequenciamento e discussão de próximos passos do projeto.

## Decisões que este documento fixa

- A evolução do produto será incremental.
- Primeiro validaremos a fundação full-stack e o núcleo jogável antes de ampliar profundidade, conteúdo e retenção.

## Etapa 1: Fundação de Produto e Arquitetura

Objetivo: consolidar documentação, linguagem de produto, estrutura inicial de frontend, backend e banco, e principais decisões técnicas.

Saídas esperadas:

- documentação de produto e engenharia
- framing claro do MVP
- stack definida
- visão arquitetural full-stack
- módulos principais de domínio mapeados
- plano de conteúdo do MVP definido

## Etapa 2: Fundação Backend e Autenticação

Objetivo: criar a base persistente da plataforma e garantir acesso autenticado ao sistema.

Saídas esperadas:

- estrutura base do backend em Spring Boot
- autenticação com JWT
- entidade `User` persistida
- configuração inicial de banco PostgreSQL
- fundação das entidades centrais

## Etapa 3: Vertical Slice Jogável Integrado

Objetivo: construir a primeira experiência fim a fim com frontend, backend, dados persistidos, ações, eventos e feedback narrativo.

Saídas esperadas:

- frontend funcional em Nuxt
- `Kingdom` carregado do backend
- ações principais do jogador
- log narrativo
- processamento inicial de `NarrativeEvent`
- persistência do `KingdomState`

## Etapa 4: CRUDs e Operação de Conteúdo

Objetivo: permitir administração estruturada das entidades essenciais da plataforma.

Saídas esperadas:

- CRUD de `User`
- CRUD de `Kingdom` e seus dados principais
- CRUD de `NarrativeEvent` e conteúdo essencial
- CRUD de `IncomeSource`
- CRUD de `ContextualTip`
- base para gerenciamento de `Quest` e `Chapter` futuros

## Etapa 5: Expansão de Conteúdo e Progressão

Objetivo: aumentar variedade, profundidade pedagógica e consistência de retorno do usuário.

Saídas esperadas:

- mais eventos e escolhas
- primeiros `Chapter` ou arcos narrativos
- melhor distribuição de risco, recompensa e progressão
- progresso mais robusto do usuário (`PlayerProgress`)

## Etapa 6: Refinamento e Evolução de Produto

Objetivo: preparar a plataforma para ciclos maiores de validação e crescimento.

Saídas esperadas:

- refinamento de UX
- observabilidade básica
- organização mais madura dos módulos
- base para analytics, operação e experimentos futuros

## Etapa 7: IA Conversacional e Personalização

Objetivo: introduzir inteligência artificial como camada de apoio personalizado ao usuário.

Saídas esperadas:

- assistente conversacional onde o usuário relata dores e dificuldades financeiras em linguagem natural
- feedback personalizado baseado no histórico e comportamento acumulado do usuário via `BehaviorLog`
- sugestões adaptadas ao perfil financeiro individual
- base de interação em linguagem natural sobre finanças pessoais

Dependência: exige histórico de comportamento acumulado nas etapas anteriores.

## Etapa 8: Analytics e Inteligência de Produto

Objetivo: transformar dados anonimizados e agregados em inteligência para o produto e para o usuário.

Saídas esperadas:

- painel interno com padrões de comportamento agregados
- insights sobre hábitos financeiros mais comuns entre os usuários
- base para publicação de relatórios ou conteúdo gerado a partir de dados reais
- refinamento de eventos e conteúdo baseado em dados de uso
- fundação para futuras features baseadas em comportamento coletivo

Dependência: exige volume mínimo de usuários e histórico de comportamento registrado desde o MVP.

## Dependências entre etapas

- A Etapa 2 depende da clareza produzida na Etapa 1.
- A Etapa 3 depende de autenticação e persistência minimamente funcionais.
- A Etapa 4 depende das entidades e relações centrais estarem estabilizadas.
- A Etapa 5 só faz sentido depois de provar que o loop central integrado funciona.
- A Etapa 6 deve acontecer apenas com mais clareza sobre adesão e potencial de continuidade.
- A Etapa 7 depende de histórico de comportamento real acumulado nas etapas anteriores.
- A Etapa 8 depende de volume mínimo de usuários para que dados agregados sejam estatisticamente relevantes.

## Decisões tomadas

- O projeto será guiado por validação progressiva.
- O roadmap privilegia fundação full-stack antes de expansão de escopo.
- O crescimento de conteúdo e operação virá depois de um núcleo integrado e persistente.

## Pontos para revisar depois

- Estimar duração de cada etapa.
- Adicionar critérios objetivos para passagem entre fases.
- Revisar a ordem se feedbacks técnicos ou de usuários apontarem outra prioridade.
