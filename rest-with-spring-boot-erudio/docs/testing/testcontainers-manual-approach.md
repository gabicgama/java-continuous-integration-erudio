# Testcontainers — Abordagem Manual

Este documento explica a abordagem utilizada originalmente no curso para integrar o Testcontainers ao Spring Boot.

A implementação utiliza `ApplicationContextInitializer` para iniciar manualmente o container MySQL e registrar suas informações de conexão no contexto do Spring.

> **Contexto:** esta abordagem é mantida neste projeto para documentar a implementação apresentada pelo curso e as adaptações necessárias para o Testcontainers 2.x.

---

## 1. Dependências

A abordagem manual utiliza o Testcontainers e o módulo específico do MySQL.

```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>testcontainers</artifactId>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>testcontainers-mysql</artifactId>
    <scope>test</scope>
</dependency>
```

Caso o ciclo de vida dos containers seja integrado ao JUnit 5, também pode ser utilizada:

```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>testcontainers-junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

O Testcontainers distribui funcionalidades em módulos separados, incluindo módulos específicos para bancos de dados.

Neste projeto, as versões são gerenciadas pelo Spring Boot, portanto não é necessário declarar manualmente a versão dessas dependências.

---

## 2. `MySQLContainer`

No Testcontainers 2.x, o `MySQLContainer` utilizado neste projeto pertence ao pacote:

```java
import org.testcontainers.mysql.MySQLContainer;
```

Além disso, a classe não deve ser utilizada como tipo genérico.

A declaração utilizada é:

```java
static final MySQLContainer mysql =
        new MySQLContainer("mysql:8.4");
```

Uma versão anterior do Testcontainers utilizava uma API diferente, sendo comum encontrar código como:

```java
MySQLContainer<?> mysql =
        new MySQLContainer<>("mysql:8.0.28");
```

Essa implementação precisa ser adaptada quando utilizada com o Testcontainers 2.x.

---

## 3. Classe `AbstractIntegrationTest`

A implementação manual possui uma estrutura semelhante a:

```java
@ContextConfiguration(
    initializers = AbstractIntegrationTest.Initializer.class
)
public class AbstractIntegrationTest {

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static final MySQLContainer mysql =
                new MySQLContainer("mysql:8.4");

        private static void startContainers() {
            Startables.deepStart(
                Stream.of(mysql)
            ).join();
        }

        private static Map<String, String> createConnectionConfiguration() {
            return Map.of(
                "spring.datasource.url", mysql.getJdbcUrl(),
                "spring.datasource.username", mysql.getUsername(),
                "spring.datasource.password", mysql.getPassword()
            );
        }

        @Override
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public void initialize(
                ConfigurableApplicationContext applicationContext) {

            startContainers();

            ConfigurableEnvironment environment =
                    applicationContext.getEnvironment();

            MapPropertySource testcontainers =
                    new MapPropertySource(
                        "testcontainers",
                        (Map) createConnectionConfiguration()
                    );

            environment.getPropertySources()
                    .addFirst(testcontainers);
        }
    }
}
```

---

## 4. Como essa implementação funciona?

A execução pode ser resumida da seguinte forma:

```text
Teste de integração
        ↓
@ContextConfiguration
        ↓
Initializer
        ↓
Startables.deepStart()
        ↓
Testcontainers
        ↓
Docker
        ↓
Container MySQL 8.4
        ↓
getJdbcUrl()
getUsername()
getPassword()
        ↓
MapPropertySource
        ↓
Spring Environment
        ↓
DataSource
        ↓
MySQL do Testcontainers
```

### 4.1. Criação do container

```java
static final MySQLContainer mysql =
        new MySQLContainer("mysql:8.4");
```

Essa declaração cria um objeto Java que representa um container MySQL baseado na imagem Docker `mysql:8.4`.

A criação do objeto não significa necessariamente que o container já esteja executando.

---

### 4.2. Inicialização

O método:

```java
private static void startContainers() {
    Startables.deepStart(
        Stream.of(mysql)
    ).join();
}
```

solicita ao Testcontainers que inicialize o container.

O Testcontainers utiliza o Docker para:

1. verificar o ambiente Docker;
2. obter a imagem caso necessário;
3. criar o container;
4. iniciar o MySQL;
5. aguardar o serviço estar disponível.

A documentação do Testcontainers descreve esse ciclo de criação, inicialização e encerramento dos containers durante os testes.

---

## 5. Configuração dinâmica do banco

Depois que o container está iniciado, o código obtém as informações de conexão:

```java
mysql.getJdbcUrl()
mysql.getUsername()
mysql.getPassword()
```

Esses valores são utilizados para criar:

```java
Map<String, String>
```

contendo:

```text
spring.datasource.url
spring.datasource.username
spring.datasource.password
```

---

## 6. `MapPropertySource`

A implementação cria uma fonte de propriedades:

```java
MapPropertySource testcontainers =
        new MapPropertySource(
            "testcontainers",
            (Map) createConnectionConfiguration()
        );
```

Depois:

```java
environment.getPropertySources()
        .addFirst(testcontainers);
```

adiciona essa fonte no início da lista de propriedades do Spring.

Isso faz com que as informações fornecidas pelo Testcontainers tenham prioridade sobre as configurações existentes.

Dessa maneira, o `application-test.yml` não precisa conhecer previamente a porta utilizada pelo container.

---

## 7. Por que isso é necessário?

O Testcontainers normalmente utiliza portas mapeadas dinamicamente. Portanto, não é recomendado assumir que o MySQL estará disponível em:

```text
localhost:3306
```

O código obtém a URL diretamente do container:

```java
mysql.getJdbcUrl()
```

e fornece essa informação ao Spring.

Isso evita configurações fixas como:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/db_default
```

para o banco utilizado exclusivamente pelos testes.

---

## 8. `application-test.yml`

Com essa abordagem, o arquivo de teste pode conter apenas as configurações que não dependem diretamente do container:

```yaml
server:
  port: 8888

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update

    show-sql: false
```

A URL, o usuário e a senha são fornecidos dinamicamente pelo `Initializer`.

---

## 9. Vantagens e limitações

### Vantagens

* Demonstra explicitamente como o Testcontainers funciona.
* Mostra como obter informações de conexão do container.
* Permite entender como propriedades podem ser adicionadas dinamicamente ao `Environment` do Spring.
* É uma abordagem útil para compreender a integração em baixo nível.

### Limitações

A implementação exige bastante código adicional:

```text
ApplicationContextInitializer
        +
Startables
        +
MapPropertySource
        +
ConfigurableEnvironment
        +
getJdbcUrl()
        +
getUsername()
        +
getPassword()
```

Além disso, o desenvolvedor precisa manter manualmente a integração entre o container e o `DataSource`.

Para versões modernas do Spring Boot, existe uma alternativa mais simples utilizando `@ServiceConnection`.

Consulte [Testcontainers — Abordagem Moderna](./testcontainers-service-connection.md).
