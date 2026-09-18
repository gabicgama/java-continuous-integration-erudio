# Spring Boot 4 — Adaptações e Migração

Este documento registra as principais adaptações realizadas neste projeto devido às diferenças entre as versões utilizadas no curso e as versões utilizadas neste projeto.

O curso utiliza **Spring Boot 3**, enquanto este projeto utiliza **Spring Boot 4** e **Java 25**.

O objetivo não é documentar todas as mudanças existentes entre as versões, mas registrar as diferenças que foram encontradas durante a implementação dos exemplos e exercícios do curso.

---

## 1. Configuração do dialeto do Hibernate

### Como era apresentado no curso

Em versões anteriores, era comum configurar explicitamente o dialeto do banco de dados utilizado pelo Hibernate:

```yaml
spring:
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
```

### Configuração utilizada neste projeto

Com as versões atuais do Hibernate utilizadas pelo Spring Boot 4, não foi necessário informar explicitamente o dialeto do MySQL.

A configuração utilizada no projeto é:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
```

O Hibernate consegue determinar o dialeto SQL apropriado a partir das informações da conexão com o banco de dados.

### Atenção: `dialect` e `ddl-auto` possuem funções diferentes

A remoção do `dialect` não significa que `ddl-auto` seja seu substituto.

São configurações com finalidades diferentes:

* `dialect`: define o dialeto SQL utilizado pelo Hibernate.
* `ddl-auto`: define o comportamento do Hibernate em relação ao schema do banco de dados.

Neste projeto, `ddl-auto: update` é utilizado para permitir que o Hibernate atualize o schema de acordo com as entidades da aplicação.

---

## 2. Separação dos starters de testes

Uma das mudanças encontradas no Spring Boot 4 foi a modularização do suporte a testes.

O `spring-boot-starter-test` continua fornecendo as bibliotecas de uso geral, como JUnit, Mockito, AssertJ e Hamcrest. Entretanto, o Spring Boot 4 passou a disponibilizar starters específicos para testes de determinadas tecnologias.

### Spring Data JPA

Para os testes relacionados ao Spring Data JPA, foi necessário adicionar explicitamente o starter específico:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa-test</artifactId>
    <scope>test</scope>
</dependency>
```

Esse starter fornece o suporte de testes específico para Spring Data JPA e Hibernate.

Essa mudança faz parte da modularização dos recursos de teste do Spring Boot 4, que passou a organizar o suporte de testes em módulos específicos por tecnologia.

---

## 3. Substituição de `@MockBean` por `@MockitoBean`

### Como era apresentado no curso

Nos exemplos baseados em Spring Boot 3, era comum encontrar:

```java
@MockBean
private PersonService service;
```

### Configuração utilizada neste projeto

No Spring Boot 4, o mecanismo utilizado para registrar mocks Mockito no `ApplicationContext` utiliza `@MockitoBean`.

Exemplo:

```java
@MockitoBean
private PersonService service;
```

A annotation `@MockitoBean` pertence ao suporte de testes do Spring Framework e é utilizada para adicionar um mock Mockito ao contexto da aplicação.

### Import

A annotation utilizada no projeto é:

```java
import org.springframework.test.context.bean.override.mockito.MockitoBean;
```

---

## 4. Mudança do `@WebMvcTest`

Outra adaptação necessária foi relacionada aos testes da camada Web/MVC.

### Dependência utilizada no projeto

No Spring Boot 4, o suporte de testes para Spring MVC possui um starter específico:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc-test</artifactId>
    <scope>test</scope>
</dependency>
```

O Spring Boot 4 disponibiliza o módulo `spring-boot-webmvc-test` especificamente para o suporte de testes MVC.

### Mudança de pacote

Além da dependência, o package da annotation `@WebMvcTest` também mudou.

No projeto, o import utilizado é:

```java
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
```

Exemplo:

```java
@WebMvcTest(PersonController.class)
class PersonControllerTest {
    
    // testes
}
```

A documentação atual do Spring Boot 4 demonstra `@WebMvcTest` sendo utilizada a partir do módulo `spring-boot-webmvc-test`.

### Utilização com `@MockitoBean`

Os testes MVC podem utilizar `@MockitoBean` para fornecer mocks das dependências do controller:

```java
@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @MockitoBean
    private PersonService service;

    // testes
}
```

Essa combinação é apresentada na documentação do Spring Boot 4 para testes de controllers com `@WebMvcTest`.

---

## Resumo das adaptações

| Item              | Curso / Spring Boot 3                       | Projeto / Spring Boot 4                                         |
| ----------------- | ------------------------------------------- | --------------------------------------------------------------- |
| Dialeto Hibernate | Configuração explícita apresentada no curso | Dialeto detectado automaticamente                               |
| `ddl-auto`        | Utilizado conforme configuração do projeto  | `update`                                                        |
| Testes JPA        | Dependências do curso                       | `spring-boot-starter-data-jpa-test`                             |
| Mock de beans     | `@MockBean`                                 | `@MockitoBean`                                                  |
| Testes MVC        | Dependências do curso                       | `spring-boot-starter-webmvc-test`                               |
| `@WebMvcTest`     | Package utilizado no curso                  | `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest` |

---

## Observações

As adaptações descritas neste documento foram identificadas durante a implementação prática do conteúdo do curso.

Novas diferenças encontradas entre o código apresentado no curso e as versões utilizadas neste projeto serão adicionadas aqui.

O objetivo é manter um registro das alterações necessárias para acompanhar um curso baseado em **Spring Boot 3** utilizando **Spring Boot 4 e Java 25**.

## Referências

* [Spring Boot — Testing](https://docs.spring.io/spring-boot/4.0/reference/testing/index.html)
* [Spring Boot — Test Slices](https://docs.spring.io/spring-boot/4.0/appendix/test-auto-configuration/slices.html)
* [Spring Boot — Build Systems](https://docs.spring.io/spring-boot/4.0/reference/using/build-systems.html)
* [Spring Blog — Modularizing Spring Boot](https://spring.io/blog/2025/10/28/modularizing-spring-boot/)
