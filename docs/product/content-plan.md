# Plano de Conteúdo — MVP

## Objetivo deste documento

Definir os temas financeiros, o primeiro pacote de eventos narrativos, a estrutura de dicas contextuais e o sistema de categorias que guiarão a produção de conteúdo do MVP.

## Quando usar

Use este documento ao escrever eventos narrativos, criar dicas contextuais, organizar conteúdo educativo e validar se o conteúdo produzido cobre os conceitos essenciais da primeira versão.

## Decisões que este documento fixa

- O MVP cobrirá cinco temas financeiros centrais.
- O primeiro pacote terá oito eventos narrativos.
- As dicas contextuais serão organizadas por seção da plataforma e por tipo de ação.
- As categorias de conteúdo serão identificadas em inglês como tags técnicas.

## Temas financeiros do MVP

Cinco temas que cobrem o essencial para as personas Invisível Rico e Endividado Sem Mapa:

| Tema | Identificador | Conceito central |
|---|---|---|
| Fluxo de caixa | `cash-flow` | Entender para onde o dinheiro vai é o ponto zero de tudo |
| Reserva de emergência | `emergency-reserve` | Primeiro colchão de segurança — sem ela tudo vira dívida |
| Dívidas e juros | `debt` | Entender o tamanho real da dívida e como priorizar |
| Fontes de renda | `income-sources` | Consciência sobre o que entra e possibilidade de diversificar |
| Consumo consciente | `conscious-spending` | Distinguir necessidade, desejo e impulso |

## Primeiro pacote de eventos narrativos

Oito `NarrativeEvent` que simulam decisões reais enfrentadas no dia a dia financeiro:

### `the-month-begins`

- **Conceito ensinado:** alocação de renda — para onde o salário vai
- **Gatilho no `Kingdom`:** início do ciclo mensal
- **Resumo narrativo:** os recursos do reino chegam. O jogador precisa decidir como distribuí-los entre necessidades, dívidas, reserva e desejos.

### `the-invisible-trap`

- **Conceito ensinado:** juros do consignado e custo real da dívida
- **Gatilho no `Kingdom`:** reino com dívida alta e sem reserva
- **Resumo narrativo:** o reino percebe que parte dos recursos some antes mesmo de chegar. Uma maldição silenciosa drena as finanças todo mês.

### `the-unexpected-crisis`

- **Conceito ensinado:** por que a reserva de emergência existe
- **Gatilho no `Kingdom`:** evento aleatório com gasto imprevisto
- **Resumo narrativo:** um desastre inesperado atinge o reino. Quem tem reserva sobrevive. Quem não tem, contrai mais dívida.

### `the-kingdom-s-temptation`

- **Conceito ensinado:** necessidade vs. desejo vs. impulso
- **Gatilho no `Kingdom`:** reino com saldo positivo
- **Resumo narrativo:** um mercador oferece um item raro e caro. O jogador decide se compra, adia ou ignora.

### `two-treasuries`

- **Conceito ensinado:** múltiplas fontes de renda — gestão e consciência
- **Gatilho no `Kingdom`:** usuário com mais de uma `IncomeSource` cadastrada
- **Resumo narrativo:** o reino descobre que tem dois fluxos de recursos. Como cada um se comporta? Como proteger ambos?

### `the-growing-burden`

- **Conceito ensinado:** juros compostos e dívida sem plano
- **Gatilho no `Kingdom`:** reino com dívida sem estratégia de quitação
- **Resumo narrativo:** a dívida do reino cresce sozinha a cada ciclo. O jogador percebe que ignorar o problema o torna maior.

### `the-wall-s-bricks`

- **Conceito ensinado:** como construir reserva de emergência progressivamente
- **Gatilho no `Kingdom`:** reino vulnerável, sem reserva
- **Resumo narrativo:** o conselheiro do reino sugere guardar pequenas quantidades todo ciclo. A muralha se constrói tijolo por tijolo.

### `the-ruler-s-choice`

- **Conceito ensinado:** priorização — quitar dívida ou investir
- **Gatilho no `Kingdom`:** reino com saldo positivo e dívida ativa
- **Resumo narrativo:** o reino tem recursos sobrando. Dois conselheiros discutem: um quer quitar a dívida, outro quer expandir o território. O jogador decide.

## Dicas contextuais por seção da plataforma

Mapa de `ContextualTip` por `TipContext`:

### Dashboard (visão geral)

- Categoria: múltiplas (`cash-flow`, `emergency-reserve`, `debt`)
- Exemplos de conteúdo:
  - "Você sabia que a maioria das pessoas não sabe para onde vai 30% da sua renda todo mês?"
  - "Ter mais de uma fonte de renda reduz sua vulnerabilidade financeira."
  - "Reserva de emergência não é luxo — é proteção."

### Seção de Fontes de Renda (`IncomeSource`)

- Categoria: `income-sources`
- Exemplos de conteúdo:
  - "Renda ativa depende do seu tempo. Renda passiva trabalha por você."
  - "Alugar um bem que você possui é uma das formas mais simples de renda extra."
  - "Freelance pode começar como renda complementar e crescer ao seu ritmo."

### Seção de Reserva de Emergência

- Categoria: `emergency-reserve`
- Exemplos de conteúdo:
  - "A reserva ideal cobre de 3 a 6 meses das suas despesas essenciais."
  - "Comece pequeno. Guardar R$100 por mês já é começar."
  - "Guarde a reserva em conta de fácil acesso, mas separada do seu dia a dia."

### Seção de Dívidas

- Categoria: `debt`
- Exemplos de conteúdo:
  - "Antes de quitar qualquer dívida, saiba o tamanho real de todas elas."
  - "O método bola de neve começa pela menor dívida para gerar motivação."
  - "Juros do cartão de crédito podem dobrar uma dívida em menos de um ano."

### Seção de Orçamento / Gastos

- Categoria: `budget`, `conscious-spending`
- Exemplos de conteúdo:
  - "A regra 50/30/20: 50% para necessidades, 30% para desejos, 20% para poupança."
  - "Categorize seus gastos por pelo menos um mês antes de cortar qualquer coisa."
  - "Necessidade é o que você precisa. Desejo é o que você quer. Impulso é o que você compra sem pensar."

### Após ação do usuário (feedback contextual)

- Categoria: varia conforme a decisão tomada
- Exemplos de conteúdo:
  - Após quitar dívida: "Boa decisão. Cada dívida quitada libera mais renda para o próximo mês."
  - Após gasto impulsivo: "Gastos não planejados são o maior inimigo do orçamento. Mas o primeiro passo é reconhecê-los."
  - Após construir reserva: "Reserva de emergência é a base de tudo. Você está no caminho certo."

## Categorias de conteúdo (`Category`)

Todas as `ContextualTip` e `NarrativeEvent` serão tagueados com uma ou mais categorias:

| Identificador | Descrição |
|---|---|
| `cash-flow` | Fluxo de caixa, controle de entrada e saída |
| `emergency-reserve` | Reserva de emergência, segurança financeira |
| `debt` | Gestão de dívidas, priorização, quitação |
| `income-sources` | Fontes de renda, diversificação, novas rendas |
| `conscious-spending` | Consumo consciente, necessidade vs. desejo |
| `interest` | Juros compostos, custo real do crédito |
| `planning` | Planejamento financeiro, orçamento, metas |

## Pontos para revisar depois

- Escrever o texto narrativo completo de cada `NarrativeEvent`.
- Criar as `Choice` e `Effect` de cada evento.
- Definir os `TriggerRule` com precisão para cada evento.
- Expandir o banco de `ContextualTip` com pelo menos 3 dicas por seção antes do lançamento.
- Validar se a linguagem das dicas e eventos está adequada para iniciantes absolutos.
