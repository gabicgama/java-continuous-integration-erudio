# Java Continuous Integration & Delivery — Erudio

Repositório utilizado para acompanhar meus estudos do curso **[Java Continuous Integration-Delivery com AWS e GitHub Actions](https://www.udemy.com/course/java-continuous-integration-continuous-delivery-aws-e-github-actions/)**.

O objetivo é aplicar, de forma prática, conceitos relacionados a **testes automatizados, integração contínua, entrega contínua, Docker, GitHub Actions e serviços da AWS**, utilizando aplicações desenvolvidas com Java e Spring Boot.

O repositório também contém adaptações do projeto original do curso para versões mais recentes do Java e do Spring Boot, permitindo comparar o ambiente utilizado nas aulas com versões atuais das tecnologias.

## Sobre o curso

O curso possui uma abordagem predominantemente prática e aborda conteúdos como:

### Testes

* JUnit 5
* Mockito
* BDD (Behaviour Driven Development)
* Testes de Repository
* Testes de Service
* Testes de Controller
* Testes de APIs REST
* Testes de integração
* Testcontainers
* MySQL com Testcontainers
* REST Assured

### Docker

* Conceitos de Docker
* Dockerfile
* Docker Compose
* Containerização de aplicações
* Docker Images
* Docker Hub

### CI/CD

* GitHub Actions
* Integração Contínua (CI)
* Entrega Contínua (CD)
* Build automatizado
* Execução automatizada de testes
* Criação e publicação de Docker Images

### AWS

* IAM
* RDS
* EC2
* ECS
* Deploy de aplicações na AWS

## Estrutura do repositório

O repositório está dividido em projetos e materiais relacionados às diferentes etapas do estudo.

```text
java-continuous-integration-erudio/
│
├── README.md
│
├── dockerizing/
│   └── README.md
│
├── rest-with-spring-boot-erudio/
│   └── README.md
│
└── teste-unitarios/
```

### `dockerizing/`

Contém o projeto baseado no **ambiente original utilizado no curso**, mantendo **Java 19 e Spring Boot 3.0.1**.

É a aplicação utilizada como base para os estudos da seção de **Dockerização da Aplicação**, incluindo Dockerfile, Docker Compose, Docker Hub e GitHub Actions.

A aplicação possui testes unitários e testes de integração e será posteriormente utilizada para gerar uma Docker Image e publicá-la no Docker Hub.

Mais informações:

[Dockerizing — README](dockerizing/README.md)

### `rest-with-spring-boot-erudio/`

Contém uma versão do projeto adaptada para tecnologias mais recentes, utilizando **Java 25 e Spring Boot 4**.

Durante o desenvolvimento foram necessárias diversas adaptações em relação ao código apresentado no curso, principalmente devido às mudanças entre Spring Boot 3 e Spring Boot 4.

As alterações e problemas encontrados durante essa atualização são documentados no próprio projeto.

Mais informações:

[REST with Spring Boot and Java — README](rest-with-spring-boot-erudio/README.md)

### `teste-unitarios/`

Diretório destinado aos estudos e exercícios relacionados especificamente a **testes unitários**.

Atualmente não possui documentação própria.

## Tecnologias estudadas

As principais tecnologias e ferramentas abordadas neste repositório são:

| Tecnologia     | Conteúdo                         |
| -------------- | -------------------------------- |
| Java           | Desenvolvimento backend          |
| Spring Boot    | Desenvolvimento de APIs REST     |
| JUnit 5        | Testes automatizados             |
| Mockito        | Testes unitários e mocks         |
| Testcontainers | Testes de integração             |
| REST Assured   | Testes de APIs REST              |
| MySQL          | Banco de dados                   |
| Docker         | Containerização                  |
| Docker Compose | Orquestração local de containers |
| Docker Hub     | Armazenamento de Docker Images   |
| GitHub Actions | Integração Contínua              |
| AWS            | Cloud Computing e deploy         |

As versões das tecnologias podem variar entre os projetos. As versões específicas estão documentadas no README de cada projeto.

## Objetivo dos estudos

Além de acompanhar o conteúdo do curso, este repositório tem como objetivo consolidar conhecimentos práticos sobre:

* Desenvolvimento backend com Java e Spring Boot;
* Testes unitários e de integração;
* Testcontainers;
* Docker e containerização;
* Docker Compose;
* Docker Hub;
* GitHub Actions;
* CI/CD;
* Cloud Computing;
* Amazon AWS.

Também faz parte do objetivo entender as diferenças e adaptações necessárias ao utilizar versões mais recentes das tecnologias apresentadas no curso.

## Progresso

O estudo será desenvolvido progressivamente conforme os conteúdos do curso forem estudados e implementados.

* [x] Configuração inicial dos projetos
* [x] Configuração do Maven
* [x] Configuração do MySQL
* [x] Spring Data JPA
* [x] JUnit 5
* [x] Mockito
* [x] Testes de Service
* [x] Testes de Controller
* [x] Testes de integração
* [x] Testcontainers
* [x] REST Assured
* [x] Docker
* [x] Docker Compose
* [ ] Docker Hub
* [ ] GitHub Actions
* [ ] CI/CD
* [ ] AWS
* [ ] Deploy

## Autor

**Gabriela Gama**
