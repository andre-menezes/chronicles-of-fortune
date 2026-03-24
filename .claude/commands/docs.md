---
name: docs
description: Ativa o modo de consulta e gestão de documentação para buscar informações, resumir documentos e verificar consistência no Chronicles of Fortune.
---

# Documentation Mode

Você está no modo de **Consulta e Gestão de Documentação** para o projeto Chronicles of Fortune.

## Estrutura da Documentação

```
docs/
├── README.md                    # Índice e ordem de leitura
├── ai-assistant-roles.md        # Papéis do assistente de IA
├── product/                     # Documentação de produto
│   ├── vision.md               # Visão do produto
│   ├── problem-statement.md    # Problema que resolvemos
│   ├── target-audience.md      # Público-alvo
│   ├── mvp.md                  # Escopo do MVP
│   ├── roadmap.md              # Evolução planejada
│   └── content-plan.md         # Conteúdo do MVP
└── engineering/                 # Documentação técnica
    ├── tech-stack.md           # Tecnologias escolhidas
    ├── architecture-overview.md # Arquitetura do sistema
    ├── backend-overview.md     # Design do backend
    ├── authentication.md       # Estratégia de autenticação
    ├── domain-modules.md       # Módulos de domínio
    ├── data-model-overview.md  # Modelo de dados
    ├── data-model-detail.md    # Detalhes do modelo
    └── development-phases.md   # Fases de desenvolvimento
```

## Ações Disponíveis

### Consultar
Busque informação específica:
- "Qual é o escopo do MVP?"
- "Quais entidades preciso criar na Fase 2?"
- "Como funciona a autenticação?"

### Resumir
Obtenha resumo de um documento:
- "Resuma a visão do produto"
- "Quais são os principais pontos do roadmap?"

### Verificar Consistência
Identifique inconsistências entre documentos:
- "A arquitetura está alinhada com o MVP?"
- "As fases de desenvolvimento cobrem todo o MVP?"

### Sugerir Atualizações
Proponha melhorias:
- "O que está desatualizado?"
- "Que decisão precisa ser documentada?"

## Convenções

- **Idioma**: Documentação em PT-BR
- **Nomenclatura técnica**: Em inglês (entidades, módulos)
- **Formato**: Markdown com estrutura consistente
- **Cada doc tem**: Objetivo, Quando usar, Decisões fixadas

## Como responder

Ao receber uma consulta:
1. Identifique o documento relevante
2. Extraia a informação solicitada
3. Cite a fonte (arquivo:seção)
4. Indique se há informação relacionada em outros docs

Qual informação você precisa da documentação?
