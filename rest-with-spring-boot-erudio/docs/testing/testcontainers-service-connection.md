# Testcontainers — Abordagem Moderna com `@ServiceConnection`

Este documento apresenta a abordagem moderna para integração entre Spring Boot e Testcontainers utilizando `@ServiceConnection`.

Essa abordagem reduz significativamente o código necessário para conectar o container ao `DataSource`, permitindo que o próprio Spring Boot descubra e configure automaticamente as informações de conexão.

O `@ServiceConnection` é fornecido pelo suporte do Spring Boot ao Testcontainers e está disponível desde o Spring Boot 3.1.

---

## 1. Dependências

Para a integração utilizada neste projeto, são necessárias as seguintes dependências.

### Spring Boot Testcontainers

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-testcontainers</artifactId>
    <scope>test</scope>
</dependency>
```

Essa dependência fornece o suporte do Spring Boot para `@ServiceConnection` e a configuração automática relacionada ao Testcontainers. O módulo `spring-boot-testcontainers` inclui, entre outras, a `ServiceConnectionAutoConfiguration`.

### Integração com JUnit 5

```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>testcontainers-junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

Essa dependência fornece a integração do Testcontainers com JUnit 5, incluindo `@Testcontainers` e `@Container`.

### Módulo MySQL

```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>testcontainers-mysql</artifactId>
    <scope>test</scope>
</dependency>
```

Esse módulo fornece o `MySQLContainer`.

O driver JDBC do MySQL continua sendo uma dependência da aplicação e não é fornecido automaticamente pelo módulo `testcontainers-mysql`.

Neste projeto, as versões das dependências são gerenciadas pelo Spring Boot.

---

## 2. Classe `AbstractIntegrationTest`

A implementação moderna pode ser reduzida para:

```java
package br.com.erudio.integrationtests.testcontainers;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

@Testcontainers
@SpringBootTest
public abstract class AbstractIntegrationTest {

    @Container
    @ServiceConnection
    static final MySQLContainer mysql =
            new MySQLContainer("mysql:8.4");
}
```

Essa implementação substitui a configuração manual apresentada na abordagem anterior.

---

## 3. `@Testcontainers`

```java
@Testcontainers
```

Integra o Testcontainers ao ciclo de vida do JUnit 5.

O Testcontainers procura campos anotados com `@Container` e gerencia o ciclo de vida desses containers.

---

## 4. `@Container`

```java
@Container
static final MySQLContainer mysql =
        new MySQLContainer("mysql:8.4");
```

Indica que o objeto deve ser gerenciado pelo Testcontainers.

Como o campo é `static`, o container é compartilhado pelos métodos de teste daquela classe e é iniciado uma vez antes dos testes da classe e encerrado após o último teste.

---

## 5. `@ServiceConnection`

A principal diferença está nesta anotação:

```java
@ServiceConnection
```

Ela informa ao Spring Boot que o container fornece um serviço que deve ser utilizado pela aplicação.

No caso:

```java
@ServiceConnection
static final MySQLContainer mysql =
        new MySQLContainer("mysql:8.4");
```

o Spring Boot identifica o `MySQLContainer` e cria as informações necessárias para estabelecer a conexão com o banco.

A API do Spring Boot define `@ServiceConnection` justamente para indicar que um container fornece um serviço ao qual a aplicação deve se conectar.

---

## 6. O que foi eliminado?

Na abordagem manual era necessário:

```java
ApplicationContextInitializer
```

além de:

```java
Startables.deepStart()
```

e:

```java
mysql.getJdbcUrl()
mysql.getUsername()
mysql.getPassword()
```

Também era necessário criar:

```java
MapPropertySource
```

e adicionar manualmente essas propriedades ao `Environment`:

```java
environment.getPropertySources()
        .addFirst(testcontainers);
```

Com `@ServiceConnection`, todo esse código deixa de ser necessário.

O Spring Boot fornece a integração através do módulo `spring-boot-testcontainers` e das `ConnectionDetails` específicas para os serviços suportados.

---

## 7. Fluxo da abordagem moderna

O fluxo passa a ser:

```text
Teste de integração
        ↓
@Testcontainers
        ↓
@Container
        ↓
Testcontainers
        ↓
Docker
        ↓
MySQL 8.4
        ↓
@ServiceConnection
        ↓
Spring Boot identifica o MySQL
        ↓
ConnectionDetails
        ↓
DataSource
        ↓
MySQL do Testcontainers
```

Dessa forma, a aplicação não precisa conhecer previamente a porta, URL ou credenciais utilizadas pelo container.

---

## 8. `application-test.yml`

A configuração pode permanecer simples:

```yaml
server:
  port: 8888

spring:
  jpa:
    hibernate:
      ddl-auto: update

    show-sql: false
```

Não é necessário configurar manualmente:

```yaml
spring:
  datasource:
    url: ...
    username: ...
    password: ...
```

Essas informações são fornecidas pela conexão criada pelo Testcontainers.

O Spring Boot recomenda `@ServiceConnection` quando existe suporte para o serviço utilizado; propriedades dinâmicas continuam sendo uma alternativa quando não existe esse suporte.

---

## 9. Comparação entre as abordagens

| Responsabilidade                | Abordagem manual                   | `@ServiceConnection`             |
| ------------------------------- | ---------------------------------- | -------------------------------- |
| Criar container                 | `new MySQLContainer(...)`          | `new MySQLContainer(...)`        |
| Gerenciar lifecycle             | `Startables` / configuração manual | `@Testcontainers` + `@Container` |
| Obter URL                       | `getJdbcUrl()`                     | Spring Boot                      |
| Obter usuário                   | `getUsername()`                    | Spring Boot                      |
| Obter senha                     | `getPassword()`                    | Spring Boot                      |
| Registrar propriedades          | `MapPropertySource`                | Spring Boot                      |
| `ApplicationContextInitializer` | Necessário                         | Não necessário                   |
| Configuração do `DataSource`    | Manual                             | Automática                       |
| Quantidade de código            | Maior                              | Menor                            |

---

## 10. Por que utilizar a abordagem moderna?

A principal vantagem é delegar ao Spring Boot uma responsabilidade que anteriormente era implementada manualmente.

Em vez de:

```text
Testcontainers
    ↓
obter informações
    ↓
criar Map
    ↓
Environment
    ↓
DataSource
```

temos:

```text
Testcontainers
    ↓
@ServiceConnection
    ↓
Spring Boot
    ↓
DataSource
```

Isso reduz código de infraestrutura e deixa o teste focado no comportamento que realmente precisa ser validado.

---

## 11. Relação com a abordagem do curso

A abordagem apresentada no curso continua sendo válida para compreender como a integração funciona, mas o projeto atual utiliza uma abordagem mais moderna disponibilizada pelo Spring Boot.

A diferença não está apenas no Testcontainers. O Spring Boot passou a oferecer suporte específico para **Service Connections**, permitindo que containers conhecidos sejam utilizados diretamente como fontes de conexão.

Para este projeto, a implementação recomendada é:

```java
@Testcontainers
@SpringBootTest
public abstract class AbstractIntegrationTest {

    @Container
    @ServiceConnection
    static final MySQLContainer mysql =
            new MySQLContainer("mysql:8.4");
}
```

---

## 12. Referências

* [Spring Boot — `@ServiceConnection`](https://docs.spring.io/spring-boot/api/java/org/springframework/boot/testcontainers/service/connection/ServiceConnection.html)
* [Spring Boot — Development-time Services / Testcontainers](https://docs.spring.io/spring-boot/reference/features/dev-services.html)
* [Testcontainers — JUnit 5](https://java.testcontainers.org/test_framework_integration/junit_5/)
* [Testcontainers — MySQL](https://java.testcontainers.org/modules/databases/mysql/)
