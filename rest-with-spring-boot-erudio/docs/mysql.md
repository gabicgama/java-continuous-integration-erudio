# MySQL com Docker Compose

Guia para configurar um banco de dados MySQL utilizando Docker Compose, persistência com Docker Volume, acesso através do DBeaver e integração com uma aplicação Java Spring Boot.

---

## 📋 Pré-requisitos

Antes de começar, tenha instalado:

* [Docker Desktop](https://www.docker.com/products/docker-desktop/)
* [DBeaver](https://dbeaver.io/)
* Java
* Maven
* Spring Boot

O Docker Desktop deve estar em execução.

---

# 🐳 1. Configurando o MySQL com Docker Compose

Crie um arquivo chamado:

```text
docker-compose.yml
```

O arquivo pode ficar na raiz do projeto:

```text
project/
├── docker-compose.yml
├── pom.xml
└── src/
```

Adicione o seguinte conteúdo:

```yaml
services:

  mysql:
    image: mysql:8.4
    container_name: mysql
    restart: "no"

    environment:
      MYSQL_ROOT_PASSWORD: 123456
      MYSQL_DATABASE: db_default
      MYSQL_USER: spring
      MYSQL_PASSWORD: 123456

    ports:
      - "3306:3306"

    volumes:
      - mysql-data:/var/lib/mysql

volumes:
  mysql-data:
```

## Configurações utilizadas

| Configuração | Valor                   | Descrição                    |
| ------------ | ----------------------- | ---------------------------- |
| Image        | `mysql:8.4`             | Imagem do MySQL utilizada    |
| Container    | `mysql`                 | Nome do container            |
| Port         | `3306:3306`             | Porta do MySQL               |
| Database     | `db_default` | Banco criado automaticamente |
| User         | `spring`                | Usuário da aplicação         |
| Password     | `123456`                | Senha do usuário             |
| Volume       | `mysql-data`            | Armazenamento persistente    |

> **Atenção:** As senhas utilizadas neste exemplo são apenas para desenvolvimento local. Em um ambiente real, utilize senhas seguras e variáveis de ambiente.

---

# ▶️ 2. Iniciar o MySQL

Abra um terminal na pasta onde está o `docker-compose.yml` e execute:

```bash
docker compose up -d
```

O Docker irá:

1. Baixar a imagem do MySQL caso ela ainda não exista;
2. Criar o volume `mysql-data`;
3. Criar o container `mysql`;
4. Inicializar o MySQL;
5. Criar o banco `db_default`;
6. Criar o usuário `spring`.

---

# 🔎 3. Verificar o container

Execute:

```bash
docker ps
```

Deverá aparecer algo semelhante a:

```text
CONTAINER ID   IMAGE      STATUS         PORTS
xxxxxxxx       mysql:8.4  Up 30 seconds  0.0.0.0:3306->3306/tcp
```

---

# 📜 4. Verificar os logs do MySQL

Para visualizar os logs:

```bash
docker logs mysql
```

Para acompanhar os logs em tempo real:

```bash
docker logs -f mysql
```

Quando aparecer:

```text
ready for connections
```

o MySQL está pronto para receber conexões.

Para sair da visualização dos logs:

```text
Ctrl + C
```

Isso não encerra o container.

---

# ⏯️ 5. Iniciar e parar o MySQL

## Parar o MySQL

```bash
docker compose stop
```

O container será parado, mas continuará existindo.

## Iniciar novamente

```bash
docker compose start
```

## Subir o container

Também é possível utilizar:

```bash
docker compose up -d
```

## Derrubar o container

```bash
docker compose down
```

O container será removido.

**O volume continuará existindo**, portanto os dados do banco serão preservados.

---

# 🔄 6. Inicialização automática

No arquivo `docker-compose.yml` está configurado:

```yaml
restart: "no"
```

Isso significa que o container não será reiniciado automaticamente.

O MySQL precisa ser iniciado manualmente:

```bash
docker compose up -d
```

## Inicialização automática

Se quiser que o Docker tente manter o MySQL em execução automaticamente, altere para:

```yaml
restart: unless-stopped
```

Exemplo:

```yaml
mysql:
  image: mysql:8.4
  container_name: mysql
  restart: unless-stopped
```

Nesse caso, quando o Docker Desktop iniciar, o Docker poderá iniciar o container automaticamente.

Para desenvolvimento, `restart: "no"` permite ter controle manual sobre quando o banco está executando.

---

# 💾 7. Persistência dos dados

O MySQL utiliza um Docker Volume:

```yaml
volumes:
  - mysql-data:/var/lib/mysql
```

O `/var/lib/mysql` é o diretório utilizado pelo MySQL dentro do container para armazenar seus dados.

O `mysql-data` é um **named volume gerenciado pelo Docker**.

A estrutura conceitual é:

```text
Docker
│
├── Container
│   └── mysql
│       └── /var/lib/mysql
│
└── Volume
    └── mysql-data
```

Isso permite remover o container sem perder os dados.

Por exemplo:

```bash
docker compose down
```

remove o container, mas mantém o volume.

Ao executar novamente:

```bash
docker compose up -d
```

o MySQL utilizará os dados existentes no volume.

---

# 🔍 8. Visualizar o volume

Para listar os volumes:

```bash
docker volume ls
```

Para obter informações sobre um volume:

```bash
docker volume inspect mysql_mysql-data
```

O nome exato pode variar dependendo do nome do projeto Docker Compose.

O `Mountpoint` será algo semelhante a:

```text
/var/lib/docker/volumes/mysql_mysql-data/_data
```

No Windows com Docker Desktop + WSL 2, esse diretório pertence ao ambiente Linux utilizado pelo Docker.

Ele não aparece normalmente como:

```text
C:\var\lib\mysql
```

Não é necessário acessar ou editar diretamente esses arquivos.

---

# 🖥️ 9. Acessar o MySQL pelo DBeaver

O MySQL não possui uma interface gráfica própria.

Para visualizar bancos, tabelas e executar SQL, pode ser utilizado o DBeaver.

Abra o DBeaver e crie uma nova conexão MySQL.

Utilize:

```text
Host:     localhost
Port:     3306
Database: db_default
Username: spring
Password: 123456
```

A conexão será:

```text
DBeaver
   │
   │ localhost:3306
   ▼
Docker
   │
   ▼
MySQL
   │
   ▼
db_default
```

---

# ⚙️ 10. Configurações do DBeaver

Durante a conexão, pode ocorrer o erro:

```text
Public Key Retrieval is not allowed
```

Para resolver:

1. Edite a conexão no DBeaver;
2. Acesse **Driver Properties**;
3. Configure:

```text
allowPublicKeyRetrieval = true
```

E:

```text
useSSL = false
```

Depois utilize **Test Connection**.

## O que essas configurações fazem?

### allowPublicKeyRetrieval

```text
allowPublicKeyRetrieval = true
```

Permite que o driver JDBC obtenha a chave pública necessária para determinados métodos de autenticação do MySQL.

### useSSL

```text
useSSL = false
```

Desativa SSL/TLS na conexão.

Para um ambiente local:

```text
DBeaver → localhost → Docker → MySQL
```

isso é normalmente aceitável.

Em ambientes de produção ou conexões através de redes não confiáveis, a configuração de criptografia deve ser analisada adequadamente.

---

# ☕ 11. Integrando o MySQL com Spring Boot

Adicione o driver do MySQL ao `pom.xml`:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

---

# ⚙️ 12. Configurando o Spring Boot

No arquivo:

```text
src/main/resources/application.properties
```

adicione:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_default
spring.datasource.username=spring
spring.datasource.password=123456

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

A URL JDBC possui:

```text
jdbc:mysql://localhost:3306/db_default
     │          │         │
     │          │         └── Banco
     │          └──────────── Porta
     └─────────────────────── MySQL
```

---

# 🧩 13. Estrutura da aplicação

Depois da configuração, a arquitetura será:

```text
┌───────────────────────────────────┐
│             Windows               │
│                                   │
│  ┌─────────────────────────────┐  │
│  │       Spring Boot           │  │
│  │                             │  │
│  │ localhost:8080              │  │
│  └──────────────┬──────────────┘  │
│                 │                 │
│                 │ JDBC            │
│                 ▼                 │
│           localhost:3306          │
│                 │                 │
│  ┌──────────────▼──────────────┐  │
│  │       Docker Container      │  │
│  │                             │  │
│  │          MySQL 8.4          │  │
│  │                             │  │
│  │         db_default          │  │
│  └──────────────┬──────────────┘  │
│                 │                 │
│                 ▼                 │
│          mysql-data               │
│          Docker Volume            │
└───────────────────────────────────┘
```

---

# 🧪 14. Testando a conexão

Depois de iniciar o MySQL:

```bash
docker compose up -d
```

inicie a aplicação Spring Boot.

Por exemplo:

```bash
mvn spring-boot:run
```

Ou execute a aplicação diretamente pelo Spring Tools Suite.

Se a configuração estiver correta, o Spring Boot deverá conseguir conectar ao MySQL.

---

# 🗄️ 15. Testando o banco pelo DBeaver

Depois que a aplicação criar suas tabelas, no DBeaver é possível executar:

```sql
SHOW DATABASES;
```

Selecionar o banco:

```sql
USE db_default;
```

Listar as tabelas:

```sql
SHOW TABLES;
```

E consultar dados:

```sql
SELECT * FROM person;
```

---

# ⚠️ 16. Removendo o banco completamente

Cuidado com este comando:

```bash
docker compose down -v
```

A opção `-v` também remove os volumes utilizados pelo Compose.

Isso significa que:

```text
Container → removido
Volume    → removido
Dados     → perdidos
```

Portanto:

```bash
docker compose down
```

é diferente de:

```bash
docker compose down -v
```

Use `down -v` somente quando realmente quiser apagar os dados persistidos.

---

# 📌 Comandos principais

| Comando                          | Função                              |
| -------------------------------- | ----------------------------------- |
| `docker compose up -d`           | Cria/inicia o MySQL                 |
| `docker compose stop`            | Para o MySQL                        |
| `docker compose start`           | Inicia um container parado          |
| `docker compose down`            | Remove o container                  |
| `docker ps`                      | Lista containers em execução        |
| `docker logs mysql`              | Mostra logs do MySQL                |
| `docker volume ls`               | Lista volumes                       |
| `docker volume inspect <volume>` | Mostra informações do volume        |
| `docker compose down -v`         | Remove container **e volume/dados** |

---

## 🔐 Configuração para desenvolvimento

Este guia utiliza:

```text
Database: db_default
User:     spring
Password: 123456
Host:     localhost
Port:     3306
```

Essas configurações são destinadas a **ambiente local de desenvolvimento**.

Para projetos reais, recomenda-se utilizar variáveis de ambiente ou arquivos `.env` para não armazenar credenciais diretamente no `docker-compose.yml` ou no código-fonte.
