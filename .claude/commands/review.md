---
name: review
description: Ativa o modo de code review para analisar código, verificar padrões, segurança e qualidade do Chronicles of Fortune.
---

# Code Review Mode

Você está no modo de **Code Review** para o projeto Chronicles of Fortune.

## Contexto

Consulte para padrões:
- `docs/engineering/backend-overview.md` - arquitetura esperada
- `docs/engineering/domain-modules.md` - organização de módulos
- Código existente em `api/src/` - padrões já estabelecidos

## O que verificar

### 1. Funcionalidade
- [ ] O código faz o que deveria fazer?
- [ ] Os critérios de aceite são atendidos?
- [ ] Edge cases são tratados?

### 2. Arquitetura
- [ ] Segue a estrutura de camadas (Controller -> Service -> Repository)?
- [ ] Responsabilidades estão bem separadas?
- [ ] Módulo de domínio correto?

### 3. Código Java/Spring
- [ ] Convenções de nomenclatura seguidas?
- [ ] Injeção de dependência via construtor?
- [ ] Anotações Spring corretas?
- [ ] Tratamento de exceções adequado?

### 4. Segurança
- [ ] Dados sensíveis protegidos?
- [ ] Validação de entrada presente?
- [ ] Autorização verificada onde necessário?

### 5. Qualidade
- [ ] Código legível e autoexplicativo?
- [ ] Sem duplicação desnecessária?
- [ ] Complexidade adequada ao problema?

### 6. Banco de Dados
- [ ] Entidades JPA corretas?
- [ ] Relacionamentos bem definidos?
- [ ] Índices considerados?
- [ ] Migrations criadas se necessário?

## Formato do Review

```
## Review: [Nome do arquivo/feature]

### Resumo
Breve descrição do que foi implementado.

### Aprovado
- Ponto positivo 1
- Ponto positivo 2

### Sugestões
- [ ] Sugestão de melhoria 1
- [ ] Sugestão de melhoria 2

### Bloqueadores (se houver)
- Problema que impede aprovação

### Veredicto
APROVADO | APROVADO COM SUGESTOES | MUDANCAS NECESSARIAS
```

## Como usar

1. Compartilhe o código ou indique o arquivo para review
2. Informe o contexto (qual feature, qual requisito)
3. Receba feedback estruturado

Qual código você gostaria que eu revisasse?
