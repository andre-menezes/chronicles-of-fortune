# MVP

## Objetivo deste documento

Definir a menor versão valiosa da plataforma que já entrega uma experiência útil, jogável e capaz de validar a proposta principal do produto.

## Quando usar

Use este documento para decidir o que entra na primeira versão, o que fica fora e como evitar crescimento precoce de escopo.

## Decisões que este documento fixa

- O MVP será uma experiência solo, web e instalável como PWA.
- O foco será validar o loop central de aprendizagem por decisão e consequência.
- O MVP passará a incluir backend real, autenticação e dados persistidos.
- O MVP não incluirá ainda monetização, multiplayer, analytics avançado ou administração extremamente completa.
- A PWA no MVP não implica suporte offline completo do jogo.
- Dicas financeiras contextuais entram como feature central desde o MVP.
- Múltiplas fontes de renda entram no modelo de dados desde o MVP.
- O rastreamento de comportamento do usuário será iniciado no MVP, com dados anonimizados e agregados preparados para analytics e IA futuros.

## Definição do MVP

O MVP será uma experiência jogável em que o usuário autentica sua conta, acessa seu reino e toma decisões financeiras que geram feedback narrativo e sistêmico, com dados persistidos em backend e banco relacional, consumidos por uma interface Nuxt com distribuição em formato PWA.

Essa primeira versão precisa provar cinco coisas:

- a fantasia do reino ajuda a aprender
- o loop de escolha e consequência é engajante
- a interface moderna torna a experiência acessível
- a base full-stack suporta autenticação, progresso e CRUDs centrais do produto
- o formato PWA reduz fricção de retorno e reforça a sensação de produto-app

## Loop principal do MVP

1. O usuário cria conta ou faz login.
2. O frontend carrega os dados persistidos do usuário e do `Kingdom`.
3. O usuário visualiza o estado atual do reino.
4. O usuário escolhe uma ação.
5. O sistema processa a ação e executa um tick narrativo.
6. Um `NarrativeEvent` pode ocorrer com consequências e escolhas adicionais.
7. O `KingdomState` é atualizado no backend e refletido no frontend.
8. O usuário percebe progresso, risco ou aprendizado e pode continuar a sessão depois.

## O que entra no MVP

- autenticação com JWT
- frontend em Nuxt
- distribuição como PWA
- backend em Spring Boot
- persistência em PostgreSQL
- painel com recursos e status do `Kingdom`
- log narrativo central
- ações principais do jogador
- engine simples de `NarrativeEvent`
- consequências sobre recursos, status e progresso
- conjunto inicial de eventos e situações financeiras
- CRUDs centrais para `User`, `Kingdom` e conteúdo essencial
- linguagem educativa incorporada ao texto e ao feedback
- múltiplas `IncomeSource` cadastradas e representadas no reino
- `ContextualTip` por seção da plataforma
- dicas acionadas após ações relevantes do usuário
- categoria de dicas sobre novas fontes de renda
- rastreamento de comportamento do usuário via `BehaviorLog` para uso futuro em analytics e IA

## O que fica fora do MVP

- multiplayer
- monetização
- editor muito avançado de conteúdo
- campanhas longas demais
- analytics e segmentação avançados
- automações administrativas sofisticadas
- políticas complexas de refresh token e segurança corporativa
- offline total com sincronização complexa

## Critérios de sucesso do MVP

- o usuário entende rapidamente o papel de cada recurso
- o loop principal é claro e pode ser repetido sem confusão
- os `NarrativeEvent` tornam os conceitos mais memoráveis
- a experiência transmite aprendizado, não apenas entretenimento
- a autenticação e o carregamento de dados funcionam sem fricção excessiva
- a base de backend e banco suporta evolução futura sem retrabalho estrutural precoce
- a instalação da PWA e o retorno ao produto fazem sentido para o uso recorrente
- as `ContextualTip` são percebidas como relevantes e não intrusivas
- o usuário consegue registrar mais de uma `IncomeSource` sem fricção

## Riscos do MVP

- a fantasia pode ficar mais forte que o objetivo educativo
- a UI pode parecer bonita, mas confusa
- os eventos podem ser repetitivos cedo demais
- o jogo pode ensinar superficialmente se os feedbacks forem rasos
- o backend pode crescer com excesso de complexidade antes da validação real do produto
- a PWA pode gerar expectativa de offline que o MVP ainda não entrega

## Decisões tomadas

- O MVP continua enxuto, mas deixa de ser frontend-only.
- O objetivo é validar direção do produto e também a fundação full-stack.
- Autenticação com JWT entra desde a primeira versão.
- Persistência deixa de ser opcional e passa a ser parte do MVP.
- O frontend será entregue com estratégia PWA, sem prometer offline completo neste primeiro ciclo.
- Dicas financeiras contextuais entram como feature central desde o MVP.
- Múltiplas fontes de renda entram no modelo de dados desde o MVP.
- O rastreamento de comportamento do usuário será iniciado no MVP, com dados anonimizados e agregados preparados para analytics e IA futuros.

## Pontos para revisar depois

- Definir critérios de retenção ou tempo de sessão desejado.
- Especificar quais conceitos financeiros precisam estar cobertos no primeiro pacote de eventos.
- Refinar exatamente quais CRUDs administrativos entram ainda no MVP.
- Definir o escopo de cache e comportamento offline da PWA.
