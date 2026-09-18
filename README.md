# Projeto — Integração e Entrega Contínuas

Projeto desenvolvido durante meus estudos do curso **"Java Continuous Integration-Delivery c. AWS e Github Actions"**.

O projeto tem como objetivo aplicar, de forma prática, conceitos relacionados a **testes automatizados, integração contínua, entrega contínua, Docker e serviços de cloud**, utilizando uma aplicação desenvolvida com Java e Spring Boot.

## Sobre o Curso

O curso possui uma abordagem predominantemente prática e aborda conceitos como:

* Testes unitários com JUnit 5 e Mockito
* Desenvolvimento de testes utilizando BDD (Behaviour Driven Development)
* Testes da camada de Repository
* Testes da camada de Service
* Testes de Controllers e APIs REST
* Testes de integração com Spring Boot
* Testcontainers
* Testes de integração utilizando MySQL
* REST Assured
* Docker
* Docker Compose
* GitHub Actions
* Integração e entrega contínuas (CI/CD)
* Amazon AWS
* RDS
* EC2
* IAM
* ECS
* Deploy de aplicações na nuvem

## Tecnologias e Versões

Embora o curso utilize versões específicas das tecnologias apresentadas nas aulas, este projeto está sendo desenvolvido utilizando versões mais recentes sempre que possível.

| Tecnologia     | Versão utilizada                  |
| -------------- | --------------------------------- |
| Java           | 25                                |
| Spring Boot    | 4.x                               |
| Maven          | Versão mais recente               |
| JUnit          | 5                                 |
| Mockito        | Versão gerenciada pelo projeto    |
| MySQL          | Conforme configuração do ambiente |
| Docker         | Versão instalada no ambiente      |
| Git            | Versão instalada no ambiente      |
| GitHub Actions | —                                 |
| AWS            | —                                 |

> As versões podem ser atualizadas ao longo do desenvolvimento do projeto.

## Adaptações em Relação ao Curso

O conteúdo do curso é baseado em **Spring Boot 3**, enquanto este projeto utiliza **Spring Boot 4** e **Java 25**.

Por esse motivo, algumas configurações, APIs, dependências e imports apresentados durante as aulas precisam ser adaptados para as versões utilizadas neste projeto.

As adaptações relevantes identificadas durante o desenvolvimento estão documentadas em:

[Spring Boot 4 — Adaptações e Migração](docs/spring-boot-4-migration.md)


## Conteúdos Praticados

### Testes

* [x] JUnit 5
* [x] Mockito
* [x] BDD (Behaviour Driven Development)
* [x] Testes de Repository
* [ ] Testes de Service
* [ ] Testes de Controller
* [ ] Testes de APIs REST
* [ ] Testes de integração
* [ ] REST Assured
* [ ] Testcontainers
* [ ] MySQL com Testcontainers

### Docker

* [ ] Conceitos básicos de Docker
* [ ] Dockerfile
* [ ] Docker Compose
* [ ] Containerização da aplicação
* [ ] Integração da aplicação com containers

### CI/CD

* [ ] Git
* [ ] GitHub Actions
* [ ] Integração Contínua (CI)
* [ ] Entrega Contínua (CD)
* [ ] Build automatizado
* [ ] Execução automatizada de testes
* [ ] Criação de imagens Docker
* [ ] Deploy automatizado

### AWS

* [ ] IAM
* [ ] RDS
* [ ] EC2
* [ ] ECS
* [ ] Deploy da aplicação na AWS

## Configuração do Ambiente

### Pré-requisitos

* Java 25
* Maven
* Docker
* Git
* Conta na AWS, quando necessário

### Banco de Dados

A aplicação utiliza **MySQL**.

As instruções para configuração do banco de dados utilizando Docker estão documentadas em:

[Configuração do MySQL](docs/mysql.md)

## Executando o Projeto

As instruções para executar a aplicação serão documentadas conforme o projeto evoluir.

## Executando os Testes

Para executar os testes automatizados:

```bash
mvn test
```

## Documentação

Documentações específicas do projeto serão armazenadas na pasta `docs`.

```text
docs/
├── mysql.md
├── spring-boot-4-migration.md
└── ...
```

## Progresso

Este projeto será desenvolvido progressivamente conforme os conteúdos do curso forem estudados e implementados.

* [x] Configuração inicial do projeto
* [x] Configuração do Maven
* [x] Configuração do MySQL
* [x] Integração com Spring Data JPA
* [x] Primeiros testes com JUnit 5
* [x] Testes com Mockito
* [x] Testes de Service
* [x] Testes de Controller
* [ ] Testes de integração
* [ ] Testcontainers
* [ ] Docker
* [ ] GitHub Actions
* [ ] CI/CD
* [ ] AWS
* [ ] Deploy

## Objetivo de Aprendizado

Além de acompanhar o conteúdo do curso, este projeto tem como objetivo consolidar conhecimentos práticos sobre:

* Desenvolvimento backend com Java e Spring Boot
* Testes automatizados
* Testes unitários e de integração
* Docker e containerização
* CI/CD
* GitHub Actions
* Cloud Computing
* Amazon AWS

## Autor

**Gabriela Gama**
