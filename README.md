# Avaliação Técnica

## 📌 Visão Geral

O **CEP Protocol Service** é uma solução baseada em microserviços que permite processar solicitações de CEP de forma assíncrona. O sistema é composto por dois serviços principais:

- **api-service**: Responsável por receber as solicitações de CEP, gerar um protocolo, armazenar a requisição e atualizar as informações do CEP após o processamento.
- **worker-service**: Consome as solicitações da fila, consulta a API ViaCEP e retorna os resultados via fila para o **api-service**, que então atualiza os registros no banco de dados.

## 🛠 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- **RabbitMQ** (mensageria)
- **PostgreSQL** (banco de dados relacional)
- **Docker e Docker Compose** (containerização e orquestração)
- **ViaCEP API** (consulta de CEP externo) → [Acesse aqui](https://viacep.com.br/#:~:text=Pesquisa%20de%20CEP,ser%C3%A1%20a%20precis%C3%A3o%20do%20resultado.&text=Os%20exemplos%20acima%20demonstram%20diferentes,um%20400%20(Bad%20Request).)
- **Swagger UI** (documentação interativa) → [Acesse aqui](http://localhost:8080/swagger-ui/index.html)

## ⚙️ Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- **Maven**
- **Docker**
- **Docker Compose**

## 🚀 Como Executar

1. Clone o repositório:

   ```bash
   git clone https://github.com/Lucas-Caldas/avaliacao-tecnica.git
   ```

2. Acesse o diretório do projeto:

   ```bash
   cd avaliacao-tecnica
   ```

3. Compile os microserviços:

   ```bash
   cd api-service
   mvn clean package
   cd ..
   cd worker-service
   mvn clean package
   cd ..
   ```

4. Execute os serviços com Docker Compose:

   ```bash
   docker-compose up --build
   ```

### 🔗 Acesso aos serviços

Após a execução, os serviços estarão disponíveis em:

- **API Service**: [http://localhost:8080](http://localhost:8080)
- **RabbitMQ Management**: [http://localhost:15672](http://localhost:15672) (usuário: `admin`, senha: `admin`)
- **PostgreSQL**: `localhost:5432` (banco: `avaliacao_db`, usuário: `postgres`, senha: `postgres`)

---

## 📡 Endpoints da API

### **1️⃣ Criar uma solicitação de CEP**

`POST /solicitacoes`

**Request:**

```json
{
  "cep": "70002900"
}
```

**Response:**

```json
{
  "protocolo": "550e8400-e29b-41d4-a716-446655440000",
  "status": "PENDENTE",
  "timestamp": "2023-05-20T10:30:00Z"
}
```

### **2️⃣ Consultar o status e resultado da solicitação**

`GET /api/cep/{protocolo}`

**Response:**

```json
{
  "id": 408,
  "protocolo": "550e8400-e29b-41d4-a716-446655440000",
  "cep": "70002900",
  "status": "PROCESSADO",
  "dadosApiExterna": {
    "cep": "70722-500",
    "logradouro": "Quadra CLN 102",
    "bairro": "Asa Norte",
    "localidade": "Brasília",
    "uf": "DF",
    "ibge": "5300108",
    "ddd": "61"
  },
  "timestampProcessamento": "2023-05-20T10:32:15Z",
  "dataCriacao": "2025-03-28T22:15:48.63116",
  "dataAtualizacao": "2025-03-28T22:15:49.112424"
}
```

## 🔄 Fluxo do Sistema

1. O cliente envia um CEP para o **api-service**.
2. O **api-service** gera um protocolo, armazena no banco de dados e envia a solicitação para o **RabbitMQ**.
3. O **worker-service** consome a mensagem da fila e consulta a **API ViaCEP**.
4. O resultado da consulta é enviado de volta via **RabbitMQ**.
5. O **api-service** recebe a resposta, atualiza o banco de dados e finaliza o processamento.

---

## 🧪 Cobertura de Testes

O projeto conta com testes automatizados para garantir a confiabilidade do sistema.

### 📌 Tecnologias de Teste Utilizadas

- **JUnit 5**
- **Mockito**



### 📈 Como Executar os Testes

Para rodar os testes automatizados, execute:

```bash
mvn test
```

Os testes incluem:

- Testes unitários para os serviços e repositórios



---

## 📁 Estrutura do Projeto

```
/avaliacao-tecnica
  /api-service
    /src/main/resources
      application.properties
    Dockerfile
  /worker-service
    /src/main/resources
      application.properties
    Dockerfile
  /docker-compose.yml
  /README.md
```

---

## 📞 Contato

📌 **Nome:** Lucas Caldas\
📧 **[E-mail](mailto\:caldas.oliva@gmail.com) :** caldas.oliva@gmail.com\
🔗 **[linkedin](https://linkedin.com/in/lucas-caldas-69869094):**


