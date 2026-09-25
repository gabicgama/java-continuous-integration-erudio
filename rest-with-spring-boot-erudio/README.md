# REST with Spring Boot and Java — Versão Atualizada

API REST baseada no projeto desenvolvido durante o curso **[Java Continuous Integration-Delivery com AWS e GitHub Actions](https://www.udemy.com/course/java-continuous-integration-continuous-delivery-aws-e-github-actions/)**.

Este projeto é uma versão **atualizada do projeto original do curso**, utilizando versões mais recentes do Java e do Spring Boot.

O objetivo é acompanhar o conteúdo apresentado nas aulas, adaptando o código quando necessário para as versões atuais das tecnologias.

## Tecnologias

| Tecnologia      | Versão                                |
| --------------- | ------------------------------------- |
| Java            | 25                                    |
| Spring Boot     | 4.x                                   |
| Maven           | Versão utilizada pelo projeto         |
| MySQL           | Conforme configuração do ambiente     |
| Spring Data JPA | Gerenciado pelo Spring Boot           |
| Spring Security | Gerenciado pelo Spring Boot           |
| Spring HATEOAS  | Gerenciado pelo Spring Boot           |
| JUnit           | 5                                     |
| Mockito         | Gerenciado pelo projeto               |
| Testcontainers  | Conforme configuração do projeto      |
| Docker          | Conforme versão instalada no ambiente |

> As versões podem ser atualizadas ao longo do desenvolvimento.

## Objetivo

O projeto tem como objetivo permitir o acompanhamento do curso utilizando um ambiente mais atual.

**Curso original:**

```text
Java 19
Spring Boot 3
```

**Projeto atualizado:**

```text
Java 25
Spring Boot 4
```

Essa atualização permite identificar, na prática, quais alterações são necessárias ao migrar uma aplicação existente para versões mais recentes do Java e do Spring Boot.

## Adaptações em relação ao curso

O conteúdo do curso foi desenvolvido utilizando principalmente **Java 19 e Spring Boot 3**, enquanto este projeto utiliza **Java 25 e Spring Boot 4**.

Por causa dessas diferenças, algumas configurações, dependências, APIs e imports apresentados nas aulas precisam ser adaptados.

Entre os pontos encontrados durante a atualização estão:

* Alterações relacionadas ao Spring Boot 4;
* Alterações em APIs e imports;
* Mudanças relacionadas aos testes;
* Alterações em configurações do Spring Data JPA;
* Alterações relacionadas ao Hibernate;
* Adaptações em `@WebMvcTest`;
* Alterações relacionadas ao `@DataJpaTest`;
* Ajustes em injeção de dependências nos testes;
* Alterações em configurações do banco de dados;
* Atualização de dependências para compatibilidade com o novo ambiente.

As alterações específicas e suas soluções estão documentadas na pasta [`docs`](docs/).

## Spring Boot 4

Uma das principais diferenças deste projeto em relação ao curso é a utilização do **Spring Boot 4**.

O projeto original foi desenvolvido para Spring Boot 3, portanto algumas abordagens utilizadas nas aulas não funcionam exatamente da mesma maneira na versão mais recente.

Entre os exemplos encontrados durante a atualização estão:

* JPA e Hibernate;
* `@DataJpaTest`;
* `@SpringBootTest`;
* `@WebMvcTest`;
* `@BootstrapWith`;
* Injeção de dependências nos testes;
* Configurações do banco de dados;
* APIs e classes deprecated.

A documentação detalhada dessas alterações está disponível em:

### Migração para Spring Boot 4

[Spring Boot 4 — Adaptações e Migração](docs/spring-boot-4-migration.md)

Esse documento reúne as principais diferenças encontradas durante a atualização do projeto de Spring Boot 3 para Spring Boot 4, incluindo problemas, causas e soluções.

## Banco de dados

A aplicação utiliza **MySQL**.

O banco pode ser executado utilizando Docker, permitindo manter o ambiente de desenvolvimento separado da instalação local do MySQL.

As instruções relacionadas à configuração do banco de dados estão documentadas em:

[Configuração do MySQL](docs/mysql.md)

## Testes

O projeto possui testes automatizados utilizando:

* JUnit 5;
* Mockito;
* Spring Boot Test;
* testes de Repository;
* testes de Service;
* testes de Controller;
* testes de integração;
* Testcontainers;
* REST Assured.

O objetivo é manter os testes funcionando após a atualização do projeto, realizando as adaptações necessárias para Java 25 e Spring Boot 4.

### Documentação dos testes

Problemas e soluções relacionados aos testes são documentados na pasta [`docs`](docs/).

Entre os assuntos documentados estão:

* Configuração dos testes de Repository;
* `@DataJpaTest`;
* `@SpringBootTest`;
* `@WebMvcTest`;
* Testcontainers;
* Testes de integração;
* Conflitos de `@BootstrapWith`.

## Documentação

As documentações específicas do projeto estão armazenadas na pasta `docs`.

```text
docs/
├── mysql.md
├── spring-boot-4-migration.md
└── ...
```

### Documentos

* [Spring Boot 4 — Adaptações e Migração](docs/spring-boot-4-migration.md)
* [Configuração do MySQL](docs/mysql.md)

Novos documentos serão adicionados conforme outros problemas, adaptações e aprendizados forem identificados durante o desenvolvimento.

## Objetivo de aprendizado

Este projeto não tem como objetivo apenas reproduzir o código apresentado no curso.

O objetivo é utilizar o projeto como exercício prático para compreender:

* Como identificar problemas de compatibilidade;
* Como interpretar erros de compilação e execução;
* Como analisar mudanças entre versões do Spring Boot;
* Como atualizar dependências Maven;
* Como adaptar testes para versões mais recentes;
* Como identificar APIs deprecated;
* Como utilizar documentação oficial para resolver problemas;
* Como manter uma aplicação existente atualizada.

## Relação com o projeto `dockerizing`

Este projeto é separado do projeto localizado em `dockerizing/`.

### `dockerizing/`

Mantém o ambiente mais próximo do utilizado no curso:

```text
Java 19
Spring Boot 3.0.1
```

Seu objetivo principal é acompanhar a etapa de **Dockerização, Docker Hub e GitHub Actions**.

### `rest-with-spring-boot-erudio`

Representa a versão atualizada do projeto:

```text
Java 25
Spring Boot 4
```

Seu objetivo principal é estudar a **modernização e adaptação de uma aplicação existente** para versões mais recentes das tecnologias.

Essa separação permite acompanhar o curso sem perder o projeto original e, ao mesmo tempo, estudar as diferenças encontradas ao utilizar versões atuais.
