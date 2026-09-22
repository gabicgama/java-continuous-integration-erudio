# Docker — Spring Boot

Este documento apresenta um guia completo para containerizar uma aplicação **Spring Boot** utilizando Docker.

O fluxo apresentado neste guia é:

```text
Código Spring Boot
       ↓
   Dockerfile
       ↓
docker build
       ↓
Imagem Docker
       ↓
Docker Registry
       ↓
Docker pull
       ↓
Container Docker
       ↓
Aplicação Spring Boot
```

---

## 1. Pré-requisitos

Antes de começar, é necessário ter:

* Java instalado
* Maven ou Maven Wrapper (`mvnw`)
* Docker Desktop
* Uma aplicação Spring Boot funcionando localmente

Para verificar o Docker:

```bash
docker --version
```

Exemplo:

```text
Docker version 28.x.x
```

Também é possível verificar se o Docker está funcionando:

```bash
docker run hello-world
```

---

# 2. Estrutura do projeto

Uma aplicação Spring Boot pode ter uma estrutura semelhante a:

```text
rest-with-spring-boot-erudio/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │
│   └── test/
│
├── .mvn/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── Dockerfile
└── README.md
```

O arquivo mais importante para o Docker será:

```text
Dockerfile
```

---

# 3. Criando o Dockerfile

Na raiz do projeto, crie um arquivo chamado:

```text
Dockerfile
```

Para uma aplicação Spring Boot utilizando Java 25, podemos utilizar:

```dockerfile
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

## Explicação

### FROM

```dockerfile
FROM eclipse-temurin:25-jre
```

Define a imagem base que será utilizada.

Neste caso, estamos utilizando o Eclipse Temurin com Java 25 JRE.

A imagem contém o necessário para **executar** a aplicação Java.

---

### WORKDIR

```dockerfile
WORKDIR /app
```

Define o diretório de trabalho dentro do container.

A partir daqui, os comandos serão executados dentro de:

```text
/app
```

---

### COPY

```dockerfile
COPY target/*.jar app.jar
```

Copia o arquivo `.jar` gerado pelo Maven para dentro da imagem.

Por isso, antes de criar a imagem, precisamos executar:

```bash
./mvnw clean package
```

No Windows:

```cmd
mvnw.cmd clean package
```

Depois disso teremos algo semelhante a:

```text
target/
└── rest-with-spring-boot-erudio-0.0.1-SNAPSHOT.jar
```

O Docker copiará esse arquivo para:

```text
/app/app.jar
```

---

### EXPOSE

```dockerfile
EXPOSE 8080
```

Documenta que a aplicação utiliza a porta:

```text
8080
```

Importante:

`EXPOSE` não publica a porta do container para o computador.

A publicação será feita posteriormente com:

```bash
-p 8080:8080
```

---

### ENTRYPOINT

```dockerfile
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Define o comando executado quando o container for iniciado.

Na prática:

```bash
java -jar app.jar
```

---

# 4. Gerando o JAR da aplicação

Antes de criar a imagem Docker, precisamos gerar o `.jar`.

No Windows:

```cmd
mvnw.cmd clean package
```

Ou, se o Maven estiver instalado:

```bash
mvn clean package
```

Para executar os testes durante o processo:

```bash
mvn clean test package
```

Depois da execução, deve existir um arquivo dentro de:

```text
target/
```

Por exemplo:

```text
target/rest-with-spring-boot-erudio-0.0.1-SNAPSHOT.jar
```

---

# 5. Criando a imagem Docker

Na raiz do projeto, onde está o `Dockerfile`, execute:

```bash
docker build -t rest-with-spring-boot-erudio .
```

A opção:

```text
-t
```

define o nome da imagem.

O ponto:

```text
.
```

informa ao Docker para utilizar o diretório atual como contexto do build.

---

# 6. Verificando a imagem

Para listar as imagens existentes:

```bash
docker images
```

Ou:

```bash
docker image ls
```

Será possível encontrar algo semelhante a:

```text
REPOSITORY                    TAG       IMAGE ID       CREATED
rest-with-spring-boot-erudio  latest    abc123...      ...
```

---

# 7. Executando um container

Agora podemos criar um container a partir da imagem:

```bash
docker run -d \
  --name rest-with-spring-boot-erudio \
  -p 8080:8080 \
  rest-with-spring-boot-erudio
```

No Windows CMD, também é possível executar em uma única linha:

```cmd
docker run -d --name rest-with-spring-boot-erudio -p 8080:8080 rest-with-spring-boot-erudio
```

## O que significa cada opção?

### `-d`

Executa o container em background:

```text
detached mode
```

---

### `--name`

Define o nome do container:

```bash
--name rest-with-spring-boot-erudio
```

---

### `-p`

Faz o mapeamento das portas:

```bash
-p 8080:8080
```

A estrutura é:

```text
PORTA_DO_HOST:PORTA_DO_CONTAINER
```

Portanto:

```text
localhost:8080
       ↓
container:8080
```

---

# 8. Verificando os containers

Para visualizar containers em execução:

```bash
docker ps
```

Exemplo:

```text
CONTAINER ID   IMAGE                         STATUS        PORTS
abc123         rest-with-spring-boot-erudio  Up 10 sec     0.0.0.0:8080->8080
```

Para visualizar inclusive containers parados:

```bash
docker ps -a
```

---

# 9. Acessando a aplicação

Se a aplicação possui um endpoint:

```text
GET /api/person
```

podemos acessá-lo através de:

```text
http://localhost:8080/api/person
```

O fluxo é:

```text
Browser/Postman
      │
      │ localhost:8080
      ↓
Docker Host
      │
      │ porta 8080
      ↓
Container
      │
      │ porta 8080
      ↓
Spring Boot
```

---

# 10. Visualizando os logs

Para visualizar os logs da aplicação:

```bash
docker logs rest-with-spring-boot-erudio
```

Para acompanhar os logs em tempo real:

```bash
docker logs -f rest-with-spring-boot-erudio
```

O `-f` significa:

```text
follow
```

---

# 11. Parando o container

Para parar:

```bash
docker stop rest-with-spring-boot-erudio
```

Verifique:

```bash
docker ps
```

O container não aparecerá mais entre os containers em execução.

Entretanto, ele ainda existe.

Podemos verificar:

```bash
docker ps -a
```

---

# 12. Iniciando novamente um container parado

Se o container já existe e está parado:

```bash
docker start rest-with-spring-boot-erudio
```

Não é necessário executar `docker run` novamente.

A diferença é:

```text
docker run
    ↓
cria um novo container
```

Enquanto:

```text
docker start
    ↓
inicia um container existente
```

---

# 13. Removendo um container

Para remover um container parado:

```bash
docker rm rest-with-spring-boot-erudio
```

Se ele estiver em execução, primeiro:

```bash
docker stop rest-with-spring-boot-erudio
```

Depois:

```bash
docker rm rest-with-spring-boot-erudio
```

Também é possível utilizar:

```bash
docker rm -f rest-with-spring-boot-erudio
```

O `-f` força a remoção.

---

# 14. Criando versões da imagem

É uma boa prática utilizar tags para identificar versões.

Por exemplo:

```bash
docker build -t rest-with-spring-boot-erudio:1.0.0 .
```

Podemos verificar:

```bash
docker images
```

E teremos:

```text
rest-with-spring-boot-erudio   1.0.0
```

Podemos criar outra versão:

```bash
docker build -t rest-with-spring-boot-erudio:1.1.0 .
```

Assim podemos manter diferentes versões:

```text
rest-with-spring-boot-erudio:1.0.0
rest-with-spring-boot-erudio:1.1.0
```

---

# 15. O que é um Docker Registry?

Uma imagem Docker normalmente precisa ser armazenada em algum lugar para que outros computadores ou servidores possam baixá-la.

Esse lugar é chamado de:

```text
Docker Registry
```

Podemos pensar nele como um repositório de imagens Docker.

Exemplos:

* Docker Hub
* GitHub Container Registry (GHCR)
* Amazon Elastic Container Registry (ECR)
* GitLab Container Registry
* Azure Container Registry

Para este exemplo utilizaremos o:

**Docker Hub**

---

# 16. Criando uma conta no Docker Hub

Crie uma conta no Docker Hub.

Depois de criar a conta, suponha que o usuário seja:

```text
meuusuario
```

O nome completo da imagem será:

```text
meuusuario/rest-with-spring-boot-erudio
```

---

# 17. Fazendo login no Docker Hub

No terminal:

```bash
docker login
```

Informe suas credenciais quando solicitado.

Depois de autenticado, podemos enviar imagens para o Docker Hub.

---

# 18. Criando uma imagem com o nome do Docker Hub

Podemos criar diretamente utilizando:

```bash
docker build -t meuusuario/rest-with-spring-boot-erudio:1.0.0 .
```

A estrutura do nome é:

```text
USUARIO/REPOSITORIO:TAG
```

Por exemplo:

```text
meuusuario/rest-with-spring-boot-erudio:1.0.0
```

---

# 19. Enviando a imagem para o Docker Hub

Depois de criar a imagem:

```bash
docker push meuusuario/rest-with-spring-boot-erudio:1.0.0
```

O Docker fará o upload das camadas da imagem para o Docker Hub.

Depois disso, a imagem poderá ser baixada por outro computador.

---

# 20. Utilizando a imagem em outro computador

Em outro computador que tenha Docker instalado:

```bash
docker pull meuusuario/rest-with-spring-boot-erudio:1.0.0
```

O Docker fará o download da imagem.

Podemos verificar:

```bash
docker images
```

A imagem estará disponível localmente.

---

# 21. Executando a imagem baixada

Depois do `pull`:

```bash
docker run -d \
  --name rest-with-spring-boot-erudio \
  -p 8080:8080 \
  meuusuario/rest-with-spring-boot-erudio:1.0.0
```

No Windows CMD:

```cmd
docker run -d --name rest-with-spring-boot-erudio -p 8080:8080 meuusuario/rest-with-spring-boot-erudio:1.0.0
```

Agora temos:

```text
Docker Hub
     │
     │ docker pull
     ↓
Docker
     │
     │ docker run
     ↓
Container
     │
     ↓
Spring Boot
```

---

# 22. Imagem x Container

É importante entender a diferença.

## Imagem

Uma imagem é um **modelo imutável** utilizado para criar containers.

Exemplo:

```text
rest-with-spring-boot-erudio:1.0.0
```

## Container

Um container é uma **instância em execução de uma imagem**.

Exemplo:

```text
rest-with-spring-boot-erudio-container
```

Podemos ter:

```text
                    ┌──────────────┐
                    │ Docker Image │
                    │     1.0.0    │
                    └──────┬───────┘
                           │
                ┌──────────┴──────────┐
                ↓                     ↓
        ┌───────────────┐     ┌───────────────┐
        │   Container   │     │   Container   │
        │       A       │     │       B       │
        └───────────────┘     └───────────────┘
```

Uma mesma imagem pode ser utilizada para criar vários containers.

---

# 23. Atualizando a aplicação

Suponha que alteramos o código Spring Boot.

O fluxo será:

```text
Alterar código
     ↓
Executar testes
     ↓
Gerar novo JAR
     ↓
Criar nova imagem
     ↓
Adicionar nova tag
     ↓
Push para Docker Hub
```

Por exemplo:

```bash
mvn clean package
```

Depois:

```bash
docker build -t meuusuario/rest-with-spring-boot-erudio:1.1.0 .
```

E:

```bash
docker push meuusuario/rest-with-spring-boot-erudio:1.1.0
```

No servidor:

```bash
docker pull meuusuario/rest-with-spring-boot-erudio:1.1.0
```

Depois podemos parar o container antigo:

```bash
docker stop rest-with-spring-boot-erudio
```

Removê-lo:

```bash
docker rm rest-with-spring-boot-erudio
```

E criar um novo:

```bash
docker run -d \
  --name rest-with-spring-boot-erudio \
  -p 8080:8080 \
  meuusuario/rest-with-spring-boot-erudio:1.1.0
```

---

# 24. Usando a tag `latest`

Também podemos utilizar:

```text
latest
```

Por exemplo:

```bash
docker build -t meuusuario/rest-with-spring-boot-erudio:latest .
```

Depois:

```bash
docker push meuusuario/rest-with-spring-boot-erudio:latest
```

E em outro computador:

```bash
docker pull meuusuario/rest-with-spring-boot-erudio:latest
```

Entretanto, para ambientes de produção, é recomendável utilizar tags de versão explícitas, por exemplo:

```text
1.0.0
1.1.0
1.2.0
```

Isso facilita identificar exatamente qual versão da aplicação está sendo executada.

---

# 25. Docker Compose

Quando a aplicação possui mais componentes, como:

* Spring Boot
* MySQL
* PostgreSQL
* Redis

podemos utilizar o Docker Compose.

Exemplo:

```yaml
services:

  app:
    image: meuusuario/rest-with-spring-boot-erudio:1.0.0
    container_name: rest-with-spring-boot-erudio
    ports:
      - "8080:8080"
    depends_on:
      - mysql

  mysql:
    image: mysql:8.4
    container_name: mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: erudio
    ports:
      - "3306:3306"
```

O arquivo normalmente será:

```text
compose.yaml
```

ou:

```text
docker-compose.yml
```

---

# 26. Iniciando com Docker Compose

Na pasta onde está o arquivo:

```bash
docker compose up -d
```

Para verificar:

```bash
docker compose ps
```

Para visualizar os logs:

```bash
docker compose logs
```

Ou:

```bash
docker compose logs -f
```

Para parar:

```bash
docker compose down
```

---

# 27. Publicando a aplicação em um servidor

O Docker permite que a mesma imagem seja executada em diferentes ambientes.

Por exemplo:

```text
Computador de desenvolvimento
          ↓
       Docker
          ↓
       Imagem
          ↓
     Docker Hub
          ↓
       Servidor
          ↓
       Docker
          ↓
      Container
          ↓
   Spring Boot
```

O servidor pode ser uma máquina virtual na nuvem.

Algumas opções:

### AWS

* Amazon EC2
* Amazon ECS
* Amazon EKS
* AWS App Runner
* Amazon ECR para armazenar imagens

### Azure

* Azure Container Apps
* Azure Container Instances
* Azure Kubernetes Service
* Azure Container Registry

### Google Cloud

* Cloud Run
* Google Kubernetes Engine
* Artifact Registry

---

# 28. Amazon ECR

No ambiente AWS, uma alternativa ao Docker Hub é o:

```text
Amazon Elastic Container Registry (ECR)
```

Ele funciona como um registry privado de imagens Docker integrado à AWS.

O fluxo passa a ser:

```text
Spring Boot
     ↓
Docker build
     ↓
Docker Image
     ↓
Amazon ECR
     ↓
AWS EC2 / ECS / etc.
     ↓
Docker Container
```

Um fluxo típico seria:

```bash
docker build -t rest-with-spring-boot-erudio:1.0.0 .
```

Depois, a imagem recebe uma tag referente ao registry:

```bash
docker tag \
  rest-with-spring-boot-erudio:1.0.0 \
  <AWS_ACCOUNT_ID>.dkr.ecr.<REGION>.amazonaws.com/rest-with-spring-boot-erudio:1.0.0
```

Depois de autenticar o Docker no ECR:

```bash
docker push \
  <AWS_ACCOUNT_ID>.dkr.ecr.<REGION>.amazonaws.com/rest-with-spring-boot-erudio:1.0.0
```

O servidor poderá então fazer:

```bash
docker pull \
  <AWS_ACCOUNT_ID>.dkr.ecr.<REGION>.amazonaws.com/rest-with-spring-boot-erudio:1.0.0
```

E executar:

```bash
docker run -d \
  --name rest-with-spring-boot-erudio \
  -p 8080:8080 \
  <AWS_ACCOUNT_ID>.dkr.ecr.<REGION>.amazonaws.com/rest-with-spring-boot-erudio:1.0.0
```

---

# 29. GitHub Actions + Docker

Um dos objetivos deste projeto é utilizar CI/CD.

Podemos automatizar todo o processo.

Por exemplo:

```text
Git Push
   ↓
GitHub
   ↓
GitHub Actions
   ↓
Executar testes
   ↓
Maven Build
   ↓
Docker Build
   ↓
Docker Image
   ↓
Docker Hub / AWS ECR
   ↓
Servidor
```

Assim, não precisamos executar manualmente:

```bash
mvn clean package
```

```bash
docker build ...
```

```bash
docker push ...
```

A pipeline pode fazer isso automaticamente.

---

# 30. Exemplo de pipeline

Um workflow do GitHub Actions pode realizar:

```yaml
name: Build and Push Docker Image

on:
  push:
    branches:
      - main

jobs:

  build:

    runs-on: ubuntu-latest

    steps:

      - name: Checkout
        uses: actions/checkout@v4

      - name: Set up Java
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '25'

      - name: Run tests
        run: ./mvnw test

      - name: Build application
        run: ./mvnw package -DskipTests

      - name: Login to Docker Hub
        uses: docker/login-action@v3
        with:
          username: ${{ secrets.DOCKERHUB_USERNAME }}
          password: ${{ secrets.DOCKERHUB_TOKEN }}

      - name: Build Docker image
        run: docker build -t ${{ secrets.DOCKERHUB_USERNAME }}/rest-with-spring-boot-erudio:latest .

      - name: Push Docker image
        run: docker push ${{ secrets.DOCKERHUB_USERNAME }}/rest-with-spring-boot-erudio:latest
```

Esse exemplo é apenas uma introdução. Em um projeto real, é recomendável trabalhar com tags de versão e separar melhor as etapas de build, testes e publicação.

---

# 31. Fluxo completo

O fluxo manual completo pode ser resumido em:

### Desenvolvimento

```bash
git checkout -b feature/minha-feature
```

Desenvolver e testar.

---

### Testes

```bash
mvnw test
```

---

### Gerar JAR

```bash
mvnw clean package
```

---

### Criar imagem

```bash
docker build \
  -t meuusuario/rest-with-spring-boot-erudio:1.0.0 \
  .
```

---

### Testar container localmente

```bash
docker run -d \
  --name rest-with-spring-boot-erudio \
  -p 8080:8080 \
  meuusuario/rest-with-spring-boot-erudio:1.0.0
```

---

### Verificar

```bash
docker ps
```

E:

```bash
docker logs -f rest-with-spring-boot-erudio
```

---

### Publicar

```bash
docker push \
  meuusuario/rest-with-spring-boot-erudio:1.0.0
```

---

### Servidor

```bash
docker pull \
  meuusuario/rest-with-spring-boot-erudio:1.0.0
```

---

### Executar

```bash
docker run -d \
  --name rest-with-spring-boot-erudio \
  -p 8080:8080 \
  meuusuario/rest-with-spring-boot-erudio:1.0.0
```

---

# 32. Comandos Docker mais utilizados

| Comando               | Função                              |
| --------------------- | ----------------------------------- |
| `docker images`       | Lista imagens                       |
| `docker pull`         | Baixa uma imagem                    |
| `docker build`        | Cria uma imagem                     |
| `docker push`         | Envia uma imagem para um registry   |
| `docker run`          | Cria e inicia um container          |
| `docker start`        | Inicia container existente          |
| `docker stop`         | Para um container                   |
| `docker restart`      | Reinicia container                  |
| `docker ps`           | Lista containers em execução        |
| `docker ps -a`        | Lista todos os containers           |
| `docker logs`         | Visualiza logs                      |
| `docker exec`         | Executa comando dentro do container |
| `docker rm`           | Remove container                    |
| `docker rmi`          | Remove imagem                       |
| `docker inspect`      | Exibe informações detalhadas        |
| `docker compose up`   | Inicia serviços do Compose          |
| `docker compose down` | Para/remove serviços do Compose     |

---

# 33. Conceitos importantes

Ao trabalhar com Docker e Spring Boot, é importante diferenciar:

```text
Dockerfile
    ↓
Instruções para construir uma imagem
```

```text
Image
    ↓
Modelo imutável da aplicação
```

```text
Container
    ↓
Instância criada a partir da imagem
```

```text
Registry
    ↓
Local onde as imagens são armazenadas
```

Exemplo:

```text
                 Dockerfile
                     │
                     ↓
              docker build
                     │
                     ↓
              Docker Image
                     │
          ┌──────────┴──────────┐
          ↓                     ↓
     Docker Hub               AWS ECR
          │                     │
          ↓                     ↓
     docker pull           docker pull
          │                     │
          └──────────┬──────────┘
                     ↓
                  Container
                     │
                     ↓
                Spring Boot
```

---

# 34. Fluxo recomendado para este projeto

Para este projeto, o fluxo de desenvolvimento pode evoluir da seguinte maneira:

```text
Java + Spring Boot
        ↓
      JUnit
        ↓
      Maven
        ↓
     Docker
        ↓
 Docker Compose
        ↓
 GitHub Actions
        ↓
 Docker Hub / AWS ECR
        ↓
       AWS
```

Isso permite estudar não apenas o desenvolvimento da aplicação Spring Boot, mas também conceitos importantes de **containerização, CI/CD e cloud deployment**.

---

# 35. Próximos passos

Depois de compreender este fluxo básico, os próximos passos recomendados são:

1. Criar o `Dockerfile`.
2. Criar a imagem localmente.
3. Executar a aplicação dentro de um container.
4. Utilizar Docker Compose para executar Spring Boot + banco de dados.
5. Publicar a imagem no Docker Hub.
6. Criar uma máquina virtual na AWS.
7. Instalar Docker no servidor.
8. Fazer `docker pull` no servidor.
9. Executar o container.
10. Automatizar o build da imagem utilizando GitHub Actions.
11. Migrar o armazenamento da imagem para Amazon ECR.
12. Automatizar o deploy da aplicação na AWS.

O objetivo final do pipeline será:

```text
Developer
    │
    │ git push
    ↓
GitHub
    │
    ↓
GitHub Actions
    │
    ├── Run Tests
    │
    ├── Maven Build
    │
    ├── Docker Build
    │
    └── Docker Push
            │
            ↓
        AWS ECR
            │
            ↓
       AWS Server
            │
            ↓
        Container
            │
            ↓
      Spring Boot API
```

Esse é o conceito fundamental de uma pipeline moderna de **CI/CD para uma aplicação Spring Boot containerizada**.
