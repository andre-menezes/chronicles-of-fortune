# Visão Conceitual do Modelo de Dados

## Objetivo deste documento

Descrever as principais entidades da plataforma e suas relações de alto nível no banco de dados.

## Quando usar

Use este documento para orientar a modelagem inicial do PostgreSQL e alinhar o entendimento do domínio antes de detalhar entidades e migrações.

## Decisões que este documento fixa

- O banco principal será PostgreSQL.
- A modelagem inicial será relacional.
- O documento será conceitual nesta rodada, sem schema detalhado.
- Os nomes das entidades serão em inglês para suporte à escala internacional.

## Entidades principais

### User

Representa a conta principal da plataforma.

Guarda, em alto nível:

- identidade da conta
- informações de autenticação e perfil
- vinculação com o progresso e com os reinos controlados

### Kingdom

Representa a instância jogável principal associada ao usuário.

Guarda, em alto nível:

- nome e identidade do reino
- recursos e atributos principais
- vínculo com progresso, eventos e histórico

### KingdomState

Representa o snapshot funcional do `Kingdom` em determinado momento da experiência.

Guarda, em alto nível:

- recursos financeiros
- status e indicadores do reino
- condições necessárias para o motor narrativo

### NarrativeEvent

Representa unidades de conteúdo que podem ser disparadas pela engine.

Guarda, em alto nível:

- metadados do evento
- regras de disparo (`TriggerRule`)
- narrativa
- escolhas possíveis (`Choice`)
- efeitos associados (`Effect`)

### Quest

Representa objetivos maiores e progressão guiada dentro da experiência.

Guarda, em alto nível:

- identificação da quest
- objetivos (`Objective`)
- recompensas (`Reward`)
- vínculo com `Chapter` ou trilhas futuras

### EducationalContent

Representa textos e materiais auxiliares que ajudam a traduzir o conceito financeiro por trás da experiência jogável.

### IncomeSource

Representa as origens de receita cadastradas pelo usuário dentro da plataforma.

Guarda, em alto nível:

- tipo de renda via `IncomeType` (salário CLT, aluguel, freelance, dividendos, outros)
- valor ou estimativa de valor
- periodicidade (mensal, variável, eventual)
- vínculo com o `User`

### ContextualTip

Representa dicas financeiras exibidas em contextos específicos da plataforma.

Guarda, em alto nível:

- texto da dica
- categoria financeira (`debt`, `budget`, `emergency-reserve`, `income-sources`, `conscious-spending`, `interest`, `planning`)
- contexto de exibição via `TipContext` (seção da plataforma ou tipo de ação que a dispara)
- nível de maturidade financeira ao qual se destina

### BehaviorLog

Representa o histórico de ações e interações do usuário na plataforma.

Guarda, em alto nível:

- tipo de ação realizada
- contexto da ação (seção da plataforma, evento em andamento, etc.)
- momento do registro
- vínculo com o `User`

*Nota: dados individuais são usados apenas para funcionalidades do próprio usuário. Análises externas e de produto utilizam exclusivamente dados agregados e anonimizados.*

## Relações de alto nível

- um `User` possui ou acessa seu `Kingdom` principal
- um `Kingdom` possui `KingdomState` e `PlayerProgress` associados
- um `Kingdom` pode vivenciar vários `NarrativeEvent` ao longo do tempo
- um `NarrativeEvent` possui `Choice` e `Effect`
- um `User` ou `Kingdom` pode estar associado a `Quest`
- `EducationalContent` pode estar vinculado a eventos, quests ou módulos específicos da plataforma
- um `User` pode ter múltiplas `IncomeSource` cadastradas
- `ContextualTip` estão associadas a seções da plataforma ou a tipos de ação do usuário via `TipContext`
- `BehaviorLog` vinculam-se ao `User`, mas são anonimizados para análise agregada de produto

## Implicações para modelagem

- a separação entre cadastro de conteúdo e execução jogável deve ser clara
- eventos devem poder ser administrados sem depender de código fixo no frontend
- o banco deve suportar tanto operação do jogo quanto manutenção administrativa da plataforma
- o modelo inicial precisa privilegiar clareza e extensibilidade, não hiper otimização precoce
- `BehaviorLog` deve ser modelado com separação clara entre dados pessoais e dados agregados

## Decisões tomadas

- PostgreSQL será o banco principal desde o início.
- A modelagem será concebida para `User`, `Kingdom`, progresso e conteúdo administrativo.
- Nomenclatura das entidades em inglês para viabilizar escala internacional.
- Este documento permanece conceitual e não substitui modelagem detalhada posterior.

## Pontos para revisar depois

- Definir cardinalidades e chaves com mais precisão.
- Decidir como histórico de eventos e progresso será persistido.
- Evoluir para um documento mais técnico de entidades e relações quando a implementação do backend começar.
- Definir estratégia de anonimização e agregação dos dados de `BehaviorLog`.
