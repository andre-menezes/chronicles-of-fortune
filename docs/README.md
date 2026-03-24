# Chronicles of Fortune
### Onde sua gestão financeira vira uma jornada

---

# Documentação da Plataforma

## Objetivo deste documento

Servir como porta de entrada da documentação do projeto e indicar a ordem de leitura recomendada.

## Quando usar

Use este arquivo ao entrar no projeto pela primeira vez ou quando precisar localizar rapidamente qual documento responde cada tipo de pergunta.

## Decisões que este documento fixa

- A documentação base da plataforma será mantida em PT-BR.
- A nomenclatura técnica (entidades, módulos, categorias, identificadores) será em inglês para suporte à escala internacional.
- Os documentos serão separados por domínio: `product/` e `engineering/`.
- Cada arquivo terá uma responsabilidade única para reduzir sobreposição.
- A plataforma será documentada como sistema full-stack, não apenas frontend.

## Ordem de leitura recomendada

1. `product/vision.md`
2. `product/problem-statement.md`
3. `product/target-audience.md`
4. `product/mvp.md`
5. `product/roadmap.md`
6. `product/content-plan.md`
7. `engineering/tech-stack.md`
8. `engineering/architecture-overview.md`
9. `engineering/backend-overview.md`
10. `engineering/authentication.md`
11. `engineering/api-validation.md`
12. `engineering/api-endpoints.md`
13. `engineering/domain-modules.md`
14. `engineering/data-model-overview.md`
15. `engineering/development-phases.md`
16. `ai-assistant-roles.md`

## Mapa da documentação

### Produto

- `product/vision.md`: define o que estamos construindo e por quê.
- `product/problem-statement.md`: descreve a lacuna que a plataforma resolve.
- `product/target-audience.md`: explica para quem a plataforma existe primeiro.
- `product/mvp.md`: delimita o escopo da primeira versão.
- `product/roadmap.md`: organiza a evolução do produto em etapas.
- `product/content-plan.md`: estrutura os temas, eventos narrativos e dicas contextuais do MVP.

### Assistente de IA

- `ai-assistant-roles.md`: define os papéis e responsabilidades do assistente de IA no projeto.

### Engenharia

- `engineering/tech-stack.md`: registra as tecnologias escolhidas e sua função.
- `engineering/architecture-overview.md`: mostra a visão arquitetural de alto nível do sistema completo.
- `engineering/backend-overview.md`: descreve o papel do backend e a separação de camadas.
- `engineering/authentication.md`: documenta a estratégia de autenticação com JWT.
- `engineering/api-validation.md`: registra as regras de validação de campos e o contrato de resposta de erro da API.
- `engineering/api-endpoints.md`: contrato completo dos endpoints da API (paths, request, response, erros).
- `engineering/domain-modules.md`: organiza os módulos de domínio e os CRUDs principais.
- `engineering/data-model-overview.md`: apresenta a visão conceitual do banco de dados.
- `engineering/development-phases.md`: transforma a estratégia em etapas práticas de desenvolvimento.

## Como manter estes documentos

- Atualize o documento dono do assunto, em vez de repetir a mesma definição em vários arquivos.
- Se uma decisão mudar, ajuste primeiro o documento principal e depois revise os documentos relacionados.
- Evite transformar estes arquivos em backlog detalhado ou documentação de marketing.
- Use os documentos de engenharia para orientar implementação, não para congelar detalhes prematuramente.

## Decisões tomadas

- O foco inicial da documentação continua sendo produto + execução.
- O projeto é guiado como plataforma full-stack com frontend, backend e banco.
- A documentação serve como apoio ao desenvolvimento manual da plataforma.
- Nomenclatura técnica em inglês para viabilizar escala internacional futura.

## Pontos para revisar depois

- Adicionar convenções de escrita para novos documentos.
- Criar um glossário (`Glossary`) quando o vocabulário do domínio ficar mais extenso.
- Avaliar a necessidade futura de uma seção de ADRs técnicos.
