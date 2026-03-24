---
name: sprint
description: Ativa o modo de planejamento de sprint para iniciar sprints, acompanhar progresso e fazer retrospectivas do Chronicles of Fortune.
---

# Sprint Planning Mode

Você está no modo de **Planejamento de Sprint** para o projeto Chronicles of Fortune.

## Contexto

Consulte:
- `docs/engineering/development-phases.md` - fase atual e critérios de avanço
- `docs/product/mvp.md` - escopo do MVP

## Ações Disponíveis

### 1. Iniciar Sprint
Defina:
- Objetivo do sprint (1 frase)
- Duração sugerida
- Itens selecionados do backlog
- Critério de sucesso

### 2. Daily Check
Responda:
- O que foi feito desde a última vez?
- O que será feito agora?
- Há algum bloqueio?

### 3. Acompanhar Progresso
Mostre:
- Itens concluídos
- Itens em andamento
- Itens pendentes
- % de conclusão

### 4. Retrospectiva
Analise:
- O que funcionou bem?
- O que pode melhorar?
- Ações para o próximo sprint

## Formato de Sprint

```
# Sprint [Número]: [Objetivo]

**Duração**: [data início] -> [data fim]
**Fase**: [fase de desenvolvimento]

## Objetivo
[Uma frase clara do que queremos alcançar]

## Itens Selecionados

| Item | Estimativa | Status | Responsável |
|------|------------|--------|-------------|
| ... | P/M/G | To Do/In Progress/Done | Dev |

## Critério de Sucesso
[Como saberemos que o sprint foi bem-sucedido]

## Notas
[Decisões, aprendizados, bloqueios]
```

## Estado Atual

**Fase Ativa**: 2 - Fundação do Backend

**Critério para avançar para Fase 3**:
- A aplicação consegue ler e gravar as entidades nucleares
- A modelagem inicial do banco está coerente com o domínio

Qual ação de sprint você gostaria de realizar?
