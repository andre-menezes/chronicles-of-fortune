---
name: backlog
description: Ativa o modo de gestão de backlog para listar, adicionar, priorizar e estimar itens de trabalho do Chronicles of Fortune.
---

# Backlog Management Mode

Você está no modo de **Gestão de Backlog** para o projeto Chronicles of Fortune.

## Contexto

Consulte para priorização:
- `docs/product/mvp.md` - escopo do MVP
- `docs/engineering/development-phases.md` - fases de desenvolvimento

## Ações Disponíveis

### Listar Backlog
Mostre os itens pendentes organizados por:
- Fase de desenvolvimento
- Prioridade (Must/Should/Could/Won't)
- Status (To Do, In Progress, Done)

### Adicionar Item
Crie novo item com:
- Título claro e acionável
- Descrição do que precisa ser feito
- Critérios de aceite
- Fase relacionada
- Prioridade sugerida

### Priorizar
Reorganize itens considerando:
- Dependências técnicas
- Valor para o usuário
- Complexidade de implementação
- Alinhamento com fase atual

### Estimar
Classifique complexidade:
- **P** (Pequeno): algumas horas
- **M** (Médio): 1-2 dias
- **G** (Grande): 3-5 dias
- **XG** (Extra Grande): precisa ser quebrado

## Formato de Item

```
## [FASE-X] Título do Item

**Prioridade**: Must | Should | Could
**Estimativa**: P | M | G | XG
**Status**: To Do | In Progress | Done

### Descrição
O que precisa ser feito e por quê.

### Critérios de Aceite
- [ ] Critério 1
- [ ] Critério 2

### Dependências
- Item que precisa estar pronto antes
```

## Estado Atual

**Fase Ativa**: 2 - Fundação do Backend

**Principais Pendências da Fase 2**:
- Entidade Kingdom
- Entidade KingdomState
- Entidade PlayerProgress
- Repositories correspondentes
- Services básicos

Qual ação você gostaria de realizar no backlog?
