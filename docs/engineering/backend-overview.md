# Visão do Backend

## Objetivo deste documento

Descrever o papel do backend na plataforma e como ele deve ser organizado em alto nível.

## Quando usar

Use este documento ao estruturar o projeto Spring Boot, decidir separação de camadas e alinhar responsabilidades do backend com o restante do sistema.

## Decisões que este documento fixa

- O backend será parte estrutural do MVP.
- O backend será implementado em Java com Spring Boot.
- A aplicação backend seguirá separação clara entre API, domínio e persistência.

## Papel do backend na plataforma

O backend será responsável por sustentar a parte persistente e operacional da plataforma. Ele não existe apenas como apoio técnico, mas como componente central para autenticação, progresso do usuário, integridade dos dados e administração de conteúdo.

## Responsabilidades do backend

- autenticar usuários (`User`)
- proteger recursos da API
- persistir dados do `User` e do `Kingdom`
- expor operações de leitura e escrita para o frontend
- concentrar regras de domínio que afetam estado persistido
- suportar CRUDs administrativos e jogáveis
- registrar `BehaviorLog` para uso futuro em analytics e IA

## Separação em camadas

### Camada de API

Responsável por receber requisições HTTP, validar entrada básica e devolver respostas ao frontend.

### Camada de aplicação ou serviço

Responsável por orquestrar casos de uso, coordenar autenticação, domínio e persistência, e definir o fluxo das operações.

### Camada de domínio

Responsável por representar entidades e regras centrais da plataforma, como `User`, `Kingdom`, `NarrativeEvent` e `PlayerProgress`.

### Camada de persistência

Responsável por mapear entidades, acessar o banco e suportar consultas e gravações com JPA/Hibernate.

## Relação entre API, domínio e persistência

A API não deve conter regra de negócio central. Os controladores devem encaminhar a operação para serviços. Os serviços devem aplicar a regra do caso de uso e delegar a persistência ao repositório. O domínio deve representar as estruturas e comportamentos centrais que o sistema precisa manter com consistência.

## O que o backend não deve absorver cedo demais

- complexidade prematura de microsserviços
- automações administrativas excessivas
- autorização muito granular antes de necessidade real
- modelagens super abstratas que atrasem a construção do MVP

## Decisões tomadas

- O backend será o núcleo de autenticação, persistência e operação de dados.
- O Spring Boot será usado como fundação da API.
- A aplicação será organizada em camadas com responsabilidade clara.

## Pontos para revisar depois

- Refinar os pacotes e módulos internos do backend.
- Decidir estratégia de DTOs, mapeadores e validação mais detalhada.
- Revisar quando as regras narrativas devem sair do frontend e migrar mais fortemente para o backend.
