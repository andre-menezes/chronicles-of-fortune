# Módulos de Domínio

## Objetivo deste documento

Mapear os módulos de negócio da plataforma e indicar quais áreas exigirão CRUD e quais representam o núcleo jogável do sistema.

## Quando usar

Use este documento para orientar modelagem do backend, organização dos pacotes e priorização de CRUDs e entidades centrais.

## Decisões que este documento fixa

- Os CRUDs serão documentados por módulo e entidade, não por endpoint.
- O domínio deve separar claramente entidades jogáveis de entidades administrativas.
- O backend deve ser organizado ao redor dos módulos centrais do produto.
- A nomenclatura técnica das entidades será em inglês para suporte à escala internacional.

## Módulos principais

### Users e autenticação

Responsabilidade:

- cadastro de conta
- login
- identificação do usuário
- controle básico de acesso

Principais entidades:

- `User`
- `Credential`
- sessão autenticada em termos de aplicação

Natureza:

- administrativa e estrutural

### Kingdom e progresso

Responsabilidade:

- estado principal do jogo
- recursos, atributos e status do reino
- progresso do usuário dentro da experiência

Principais entidades:

- `Kingdom`
- `KingdomState`
- `PlayerProgress`

Natureza:

- jogável

### Narrative Events

Responsabilidade:

- cadastro e manutenção de eventos
- requisitos, escolhas e consequências
- apoio ao motor narrativo da plataforma

Principais entidades:

- `NarrativeEvent`
- `Choice`
- `Effect`
- `TriggerRule`

Natureza:

- administrativa para autoria e jogável para execução

### Quests e capítulos

Responsabilidade:

- estruturar jornadas de aprendizado e progressão futura
- agrupar objetivos, recompensas e arcos narrativos

Principais entidades:

- `Quest`
- `Objective`
- `Reward`
- `Chapter`

Natureza:

- administrativa e jogável

### Educational Content

Responsabilidade:

- textos, descrições, explicações educativas e outros materiais de suporte

Principais entidades:

- `EducationalContent`
- `Glossary`
- metadados de apoio

Natureza:

- administrativa

### Income Sources

Responsabilidade:

- cadastro de múltiplas fontes de renda associadas ao usuário
- categorização por tipo (salário CLT, aluguel, freelance, dividendos, outros)
- vínculo com o estado financeiro do `Kingdom` e com o progresso
- base para dicas sobre diversificação e criação de novas fontes de renda

Principais entidades:

- `IncomeSource`
- `IncomeType` (enum: `SALARY`, `RENTAL`, `FREELANCE`, `DIVIDENDS`, `OTHER`)

Natureza:

- administrativa para cadastro e jogável para representação no reino

### Contextual Tips

Responsabilidade:

- armazenar e exibir dicas financeiras por contexto da plataforma
- dicas associadas a seções específicas da interface (dashboard, reserva de emergência, dívidas, etc.)
- dicas acionadas após ações relevantes do usuário
- categorias: `CASH_FLOW`, `EMERGENCY_RESERVE`, `DEBT`, `INCOME_SOURCES`, `CONSCIOUS_SPENDING`, `INTEREST`, `PLANNING`

Principais entidades:

- `ContextualTip`
- `TipContext` (enum: `DASHBOARD`, `INCOME_SOURCE`, `EMERGENCY_RESERVE`, `DEBT`, `BUDGET`, `POST_ACTION`)
- `TipCategory` (enum)

Natureza:

- administrativa para autoria e jogável para exibição contextual

### Behavior Tracking

Responsabilidade:

- registrar ações, escolhas e padrões de uso do usuário ao longo do tempo
- base para analytics agregado e anonimizado
- fundação para IA e personalização futuros
- dados sensíveis individuais jamais expostos externamente; análises externas usam apenas dados anonimizados e agregados

Principais entidades:

- `BehaviorLog`
- `UserAction` (enum: `KINGDOM_CREATED`, `NARRATIVE_CHOICE_RESOLVED`, `INCOME_SOURCE_CREATED`, `INCOME_SOURCE_UPDATED`, `INCOME_SOURCE_DELETED`, `TIPS_VIEWED`)

Natureza:

- estrutural e analítica; sem exposição direta ao usuário no MVP

## CRUDs prioritários nesta rodada de produto

- `User`
- `Kingdom` e estado principal
- `NarrativeEvent`
- `IncomeSource`
- `ContextualTip`
- base inicial de `Quest` e conteúdo, quando entrarem de fato no fluxo do MVP ou logo após ele

## Entidades administrativas vs jogáveis

### Administrativas

- `User`
- `EducationalContent`
- `NarrativeEvent` como item de cadastro
- `Quest` como item de cadastro
- `Chapter`
- `ContextualTip`
- `IncomeSource`

### Jogáveis

- `Kingdom`
- `KingdomState`
- `PlayerProgress`
- `NarrativeEvent` em execução
- `Choice` e consequências vividas pelo usuário
- `IncomeSource` representada no reino

## Decisões tomadas

- O backend será guiado por módulos de domínio claros.
- CRUDs serão pensados por entidade e responsabilidade, não por tela.
- O módulo `Kingdom` será o centro do núcleo jogável.
- Nomenclatura técnica das entidades em inglês para viabilizar escala internacional.

## Pontos para revisar depois

- Refinar a fronteira entre `PlayerProgress` e `KingdomState`.
- Definir se `Quest` entra no mesmo momento que eventos administrativos.
- Evoluir este documento para um mapa mais detalhado de entidades quando a modelagem começar.
