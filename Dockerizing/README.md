# REST with Spring Boot and Java — Dockerizing

API REST desenvolvida durante o curso **[Java Continuous Integration-Delivery com AWS e GitHub Actions](https://www.udemy.com/course/java-continuous-integration-continuous-delivery-aws-e-github-actions/)**.

Este projeto mantém o ambiente utilizado na seção do curso que aborda a **Dockerização da Aplicação**, utilizando **Java 19 e Spring Boot 3.0.1**.

A aplicação é uma API REST simples, porém possui uma quantidade significativa de testes unitários e de integração. Ela será utilizada como base para os estudos de **Docker, Docker Compose, Docker Hub e Integração Contínua com GitHub Actions**.

## Tecnologias

| Tecnologia        | Versão                                   |
| ----------------- | ---------------------------------------- |
| Java              | 19                                       |
| Spring Boot       | 3.0.1                                    |
| Maven             | Gerenciado pelo projeto                  |
| MySQL             | 8.x                                      |
| Spring Data JPA   | Gerenciado pelo Spring Boot              |
| Spring Security   | Gerenciado pelo Spring Boot              |
| Spring HATEOAS    | Gerenciado pelo Spring Boot              |
| Springdoc OpenAPI | 2.0.2                                    |
| Flyway            | Gerenciado pelo Spring Boot              |
| Dozer             | 6.4.0                                    |
| Java JWT          | 3.18.3                                   |
| Mockito           | Gerenciado pelo Spring Boot              |
| Rest Assured      | 4.5.0                                    |
| Testcontainers    | 1.21.4                                   |
| Docker            | Utilizado para containerização           |
| Docker Compose    | Utilizado para execução da stack         |
| Docker Hub        | Utilizado para armazenamento das imagens |

## Principais recursos

A aplicação utiliza:

* API REST com Spring Boot;
* Persistência de dados com Spring Data JPA;
* Banco de dados MySQL;
* Migração de banco de dados com Flyway;
* Autenticação e segurança com Spring Security;
* JWT para autenticação;
* HATEOAS;
* Validação de dados;
* Documentação da API com OpenAPI/Swagger;
* Serialização XML e YAML com Jackson;
* Mapeamento de objetos com Dozer;
* Testes unitários;
* Testes de integração;
* Testcontainers;
* Rest Assured.

## Testes

O projeto possui diferentes tipos de testes:

* **Testes unitários**, utilizando JUnit e Mockito;
* **Testes de Repository**;
* **Testes de Service**;
* **Testes de Controller**;
* **Testes de integração**, utilizando o contexto do Spring Boot;
* **Testes de integração com Testcontainers**, utilizando um container MySQL;
* **Testes de API**, utilizando Rest Assured.

Os testes de integração permitem validar a aplicação em um ambiente mais próximo de sua execução real, incluindo a utilização de um banco de dados MySQL executado em container.

## Observação sobre o Testcontainers

O projeto originalmente utilizava uma versão mais antiga do Testcontainers.

Ao executar os testes com o ambiente Docker atual, ocorreu o seguinte erro:

```text
Could not find a valid Docker environment.
```

O Docker estava funcionando normalmente. O problema estava relacionado à compatibilidade entre a versão antiga do Testcontainers e o ambiente Docker utilizado.

A versão do Testcontainers foi atualizada para:

```xml
<testcontainers.version>1.21.4</testcontainers.version>
```

A atualização também trouxe uma versão mais recente do `docker-java`, e os testes de integração passaram a funcionar normalmente.

### Ambiente utilizado

* Java 19
* Spring Boot 3.0.1
* Testcontainers 1.21.4
* Docker Engine 29.7.2
* Docker Desktop 4.88.1
* WSL2

Não foi necessário alterar:

* `DOCKER_HOST`;
* `daemon.json`;
* configurações do Docker Desktop.

A atualização do Testcontainers foi suficiente para solucionar o problema.

> **Observação:** a versão `1.21.4` foi mantida neste projeto especificamente para garantir a compatibilidade com o ambiente Docker utilizado durante os estudos.

## Dockerização

Este projeto será utilizado como aplicação-base para a seção de **Dockerização da Aplicação** do curso.

A seção aborda, progressivamente:

1. Conhecimento da aplicação;
2. Criação do `Dockerfile`;
3. Integração da aplicação com Docker Compose;
4. Execução da stack utilizando Docker Compose;
5. Testes da aplicação com Postman e testes de integração;
6. Criação e utilização de Docker Images;
7. Publicação das imagens no Docker Hub;
8. Execução da aplicação a partir das imagens armazenadas no Docker Hub.

## Integração Contínua

Após a dockerização, o projeto será utilizado para implementar um pipeline de **Integração Contínua (CI)** utilizando GitHub Actions.

O pipeline deverá automatizar etapas como:

* Build da aplicação;
* Execução dos testes;
* Criação da Docker Image;
* Publicação da imagem no Docker Hub;
* Utilização da imagem publicada para executar a aplicação.

## Objetivo do projeto

O objetivo deste projeto é servir como uma aplicação prática para estudar o ciclo:

**Desenvolvimento → Testes → Dockerização → Publicação da imagem → Integração Contínua**

A aplicação será utilizada para compreender como uma aplicação Java/Spring pode ser empacotada em uma Docker Image, publicada no Docker Hub e integrada a um pipeline automatizado com GitHub Actions.

## Curso

Este projeto faz parte do curso:

**Java Continuous Integration-Delivery com AWS e GitHub Actions**

Seção relacionada à Dockerização:

* Conhecendo a aplicação;
* Criando o Dockerfile;
* Docker Compose;
* Executando a stack;
* Testando a aplicação;
* Docker Hub;
* Publicando Docker Images;
* GitHub Actions;
* Integração Contínua;
* Executando a stack a partir das Docker Images do Docker Hub.
