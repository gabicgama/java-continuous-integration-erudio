# Conflito entre `@DataJpaTest` e `@SpringBootTest` com Testcontainers

## Contexto

Ao migrar os testes de integração de um banco H2 para um banco MySQL executado através do Testcontainers, foi criada uma classe base para centralizar a configuração do container:

```java
@Testcontainers
@SpringBootTest
public abstract class AbstractIntegrationTest {

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.4");
}
```

Essa abordagem funcionava normalmente para testes de integração completos, como o teste de integração do Swagger.

Porém, ao utilizar a mesma classe base em um teste de repository com `@DataJpaTest`, ocorreu o seguinte erro:

```text
java.lang.IllegalStateException: Configuration error:
found multiple declarations of @BootstrapWith for test class
[br.com.erudio.repository.PersonRepositoryTest]:

[
@org.springframework.test.context.BootstrapWith(
    org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTestContextBootstrapper.class
),

@org.springframework.test.context.BootstrapWith(
    org.springframework.boot.test.context.SpringBootTestContextBootstrapper.class
)
]
```

---

# 1. O problema

O teste de repository estava configurado desta forma:

```java
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonRepositoryTest extends AbstractIntegrationTest {
    
    // testes...
}
```

Enquanto a classe base possuía:

```java
@Testcontainers
@SpringBootTest
public abstract class AbstractIntegrationTest {

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.4");
}
```

Isso fazia com que o `PersonRepositoryTest` recebesse **duas configurações diferentes de bootstrap do Spring Test**.

### `@DataJpaTest`

A anotação:

```java
@DataJpaTest
```

é um **test slice** voltado especificamente para a camada JPA.

Ela utiliza internamente:

```text
DataJpaTestContextBootstrapper
```

Seu objetivo é carregar somente os componentes necessários para testar a camada de persistência.

---

### `@SpringBootTest`

Por outro lado:

```java
@SpringBootTest
```

utiliza:

```text
SpringBootTestContextBootstrapper
```

e tem como objetivo carregar o contexto completo da aplicação Spring Boot.

---

### O conflito

Como `PersonRepositoryTest` herdava de `AbstractIntegrationTest`, o Spring acabava encontrando:

```text
@DataJpaTest
       ↓
DataJpaTestContextBootstrapper

@SpringBootTest
       ↓
SpringBootTestContextBootstrapper
```

O Spring não pode utilizar os dois bootstrappers simultaneamente.

Por isso ocorreu:

```text
found multiple declarations of @BootstrapWith
```

---

# 2. Por que o `SwaggerIntegrationTest` funcionava?

O teste de Swagger estava configurado assim:

```java
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class SwaggerIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("JUnit test for Should Display Swagger UI Page")
    void testShouldDisplaySwaggerUiPage() {

        var content = given()
            .basePath("/swagger-ui/index.html")
            .port(TestConfigs.SERVER_PORT)
            .when()
                .get()
            .then()
                .statusCode(200)
            .extract()
                .body()
                    .asString();

        assertTrue(content.contains("Swagger UI"));
    }
}
```

Nesse caso, tanto a classe base quanto a classe de teste utilizavam `@SpringBootTest`.

Portanto, não havia conflito entre bootstrappers:

```text
AbstractIntegrationTest
        ↓
@SpringBootTest
        ↓
SpringBootTestContextBootstrapper

SwaggerIntegrationTest
        ↓
@SpringBootTest
        ↓
SpringBootTestContextBootstrapper
```

O teste podia ser executado normalmente.

---

# 3. O Testcontainers não era o problema

O log de execução confirmou que o Testcontainers estava funcionando corretamente.

O Docker foi detectado:

```text
Found Docker environment with local Npipe socket
```

O Testcontainers conseguiu se conectar ao Docker:

```text
Connected to docker:

Server Version: 29.7.2
API Version: 1.55
Operating System: Docker Desktop
```

O Ryuk foi iniciado:

```text
Ryuk started - will monitor and terminate Testcontainers containers
```

E o MySQL foi criado e iniciado:

```text
Creating container for image: mysql:8.4
```

Depois:

```text
Container mysql:8.4 started in PT11.149543S
```

Finalmente:

```text
Container is started
(JDBC URL: jdbc:mysql://localhost:57900/test)
```

Portanto, o fluxo:

```text
Java
 ↓
Testcontainers
 ↓
Docker Desktop
 ↓
MySQL 8.4
```

estava funcionando corretamente.

O problema acontecia posteriormente, durante a configuração do contexto de testes do Spring.

---

# 4. Solução

A solução é remover `@SpringBootTest` da classe base.

A classe `AbstractIntegrationTest` deve fornecer somente a infraestrutura compartilhada do Testcontainers:

```java
@Testcontainers
public abstract class AbstractIntegrationTest {

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.4");
}
```

Agora a classe base não determina qual tipo de teste Spring deve ser utilizado.

Cada teste fica responsável por definir seu próprio tipo de contexto.

---

# 5. `PersonRepositoryTest`

O teste de repository pode continuar utilizando:

```java
@DataJpaTest
@AutoConfigureTestDatabase(
    replace = AutoConfigureTestDatabase.Replace.NONE
)
class PersonRepositoryTest extends AbstractIntegrationTest {

    // testes...
}
```

Nesse caso, o bootstrap será:

```text
@DataJpaTest
      ↓
DataJpaTestContextBootstrapper
      ↓
Contexto JPA
      ↓
MySQL do Testcontainers
```

O:

```java
@AutoConfigureTestDatabase(
    replace = AutoConfigureTestDatabase.Replace.NONE
)
```

é importante porque queremos que o teste utilize o MySQL fornecido pelo Testcontainers em vez de substituir o datasource por um banco embutido.

---

# 6. `SwaggerIntegrationTest`

O teste de integração completo pode continuar utilizando:

```java
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class SwaggerIntegrationTest extends AbstractIntegrationTest {

    // testes...
}
```

Nesse caso:

```text
@SpringBootTest
      ↓
SpringBootTestContextBootstrapper
      ↓
Contexto completo da aplicação
      ↓
MySQL do Testcontainers
```

---

# 7. Estrutura final

A arquitetura passa a ser:

```text
                    AbstractIntegrationTest
                             │
                             │
                    ┌────────┴────────┐
                    │                 │
             @DataJpaTest       @SpringBootTest
                    │                 │
                    ▼                 ▼
        PersonRepositoryTest   SwaggerIntegrationTest
```

Enquanto o Testcontainers fica compartilhado:

```text
AbstractIntegrationTest
        │
        ├── @Testcontainers
        │
        └── @Container
              │
              ▼
        MySQLContainer
              │
              ▼
           MySQL 8.4
              │
              ▼
       @ServiceConnection
              │
              ▼
       Spring DataSource
```

Essa separação permite que cada teste escolha o tipo de contexto apropriado sem que a classe base imponha um bootstrap específico.

---

# 8. Por que `@ServiceConnection` é importante?

A anotação:

```java
@ServiceConnection
```

permite que o Spring Boot reconheça automaticamente o container como uma fonte de conexão para a aplicação.

Com:

```java
@Container
@ServiceConnection
static final MySQLContainer<?> mysql =
        new MySQLContainer<>("mysql:8.4");
```

não é necessário configurar manualmente:

```properties
spring.datasource.url=...
spring.datasource.username=...
spring.datasource.password=...
```

O Spring Boot obtém essas informações diretamente do container.

Por exemplo, durante a execução o Testcontainers pode disponibilizar:

```text
jdbc:mysql://localhost:57900/test
```

e o Spring Boot utiliza automaticamente essa configuração.

---

# 9. Configuração final recomendada

## `AbstractIntegrationTest`

```java
@Testcontainers
public abstract class AbstractIntegrationTest {

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.4");
}
```

## `PersonRepositoryTest`

```java
@DataJpaTest
@AutoConfigureTestDatabase(
    replace = AutoConfigureTestDatabase.Replace.NONE
)
class PersonRepositoryTest extends AbstractIntegrationTest {

    // testes do repository
}
```

## `SwaggerIntegrationTest`

```java
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class SwaggerIntegrationTest extends AbstractIntegrationTest {

    // testes de integração
}
```

---

# 10. Regra prática

A classe base compartilhada deve conter apenas configurações que realmente possam ser compartilhadas entre diferentes tipos de teste.

Neste caso:

```java
@Testcontainers
@Container
@ServiceConnection
```

podem ser compartilhados.

Já:

```java
@SpringBootTest
```

não deve ficar na classe base porque ele define o tipo de bootstrap do teste.

Isso permite utilizar a mesma infraestrutura para diferentes tipos de teste:

| Tipo de teste       | Anotação          | Contexto            |
| ------------------- | ----------------- | ------------------- |
| Repository          | `@DataJpaTest`    | JPA                 |
| Integração completa | `@SpringBootTest` | Aplicação completa  |
| MVC                 | `@WebMvcTest`     | Camada Web          |
| Teste unitário      | `@Test`           | Sem contexto Spring |

Todos podem, quando necessário, herdar da mesma infraestrutura do Testcontainers sem entrar em conflito.

---

# Resumo

O problema não estava no Docker, no MySQL ou no Testcontainers.

O problema era a herança de:

```java
@SpringBootTest
```

pela classe:

```java
PersonRepositoryTest
```

que também utilizava:

```java
@DataJpaTest
```

Isso fazia o Spring encontrar dois `@BootstrapWith` diferentes.

A solução é manter o `AbstractIntegrationTest` responsável somente pelo Testcontainers:

```java
@Testcontainers
public abstract class AbstractIntegrationTest {

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.4");
}
```

e deixar cada teste escolher explicitamente seu próprio tipo de contexto:

```java
@DataJpaTest
```

ou:

```java
@SpringBootTest
```

Dessa forma, o Testcontainers é compartilhado sem que diferentes estratégias de bootstrap do Spring entrem em conflito.
