# 🚴 Foody Delivery - Mini Rastreador de Pedidos (Backend API)

API RESTful desenvolvida em **Java 21** e **Spring Boot 3** para o desafio técnico de Desenvolvedor Full-Stack Pleno na **Foody Delivery**.

A aplicação gerencia pedidos de delivery, seu fluxo de status (`RECEBIDO` ➔ `EM_PREPARO` ➔ `SAIU_PARA_ENTREGA` ➔ `ENTREGUE` / `CANCELADO`), autenticação segura via JWT, histórico de auditoria completo e persistência em banco relacional de zero instalação (**H2 Database Embedded** / **SQLite**).

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Java 21 / 25
- **Framework Principal**: Spring Boot 3.3.4
- **Segurança & JWT**: Spring Security 6 + JJWT `0.12.6` (Segredo criptográfico de 512 bits)
- **Persistência de Dados**: Spring Data JPA / Hibernate com H2 Database armazenado em arquivo local (`./data/foody_delivery.db`)
- **Documentação de API**: Springdoc OpenAPI 3 (Swagger UI)
- **Validação de Dados**: Bean Validation (Jakarta Validation)
- **Gerenciamento de Variáveis**: Dotenv Java (`io.github.cdimascio:dotenv-java`)
- **Produtividade**: Lombok
- **Testes Automatizados**: JUnit 5 + Mockito
- **Build Tool**: Maven Wrapper (`mvnw.cmd` / `./mvnw`)

---

## 📁 Estrutura de Pacotes

```text
com.foody.delivery
 ├── common                     # Configurações globais e tratamento de exceções
 │    ├── config                # Configuração do Swagger / OpenAPI 3
 │    └── exception             # Exceptions customizadas e GlobalExceptionHandler (HTTP 400, 401, 404, 500)
 ├── domain                     # Camada de Domínio e Entidades JPA
 │    ├── model                 # User, Order, OrderItem, OrderStatusHistory, OrderStatus, UserRole
 │    └── repository            # Interfaces Spring Data JPA (UserRepository, OrderRepository, OrderStatusHistoryRepository)
 ├── security                   # Módulo de Autenticação e Criptografia
 │    ├── config                # SecurityConfig (Stateless, CORS para React, BCrypt)
 │    ├── controller            # AuthController (/api/auth/register, /api/auth/login)
 │    ├── dto                   # RegisterRequest, LoginRequest, AuthResponse
 │    ├── jwt                   # JwtService e JwtAuthenticationFilter
 │    └── service               # AuthService e CustomUserDetailsService
 └── order                      # Módulo da API REST de Pedidos
      ├── controller            # OrderController (/api/orders)
      ├── dto                   # CreateOrderRequest, OrderResponse, UpdateOrderStatusRequest, etc.
      └── service               # OrderService (Regras de negócio, cálculo de totais e auditoria)
```

---

## 🚀 Como Executar o Projeto Backend

### Pré-requisitos
- **Java 21** ou superior instalado (Java 25 pré-detectado no ambiente).
- Não é necessário ter o Maven instalado globalmente (o projeto inclui o **Maven Wrapper** `mvnw.cmd`).

### 1. Configurar Variáveis de Ambiente (`.env`)
O projeto já conta com o arquivo `.env` configurado em `backend/.env`. Se desejar personalizar a porta ou a chave secreta:
```env
PORT=8080
DB_URL=jdbc:h2:file:./data/foody_delivery;DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER=TRUE
JWT_SECRET=65F+lflcf4OI5Au5df7G/zw7aL6wQrdSw8njGJS/H8qJfrlnJ8k3wV6qXTfWAGyYtW2oyTlzVM5lRQsB5H+YCg==
JWT_EXPIRATION_MS=86400000
```

### 2. Compilar e Rodar o Servidor
No terminal, entre na pasta `backend` e execute:

#### No Windows (PowerShell / Command Prompt):
```powershell
.\mvnw.cmd spring-boot:run
```

#### No Linux / macOS:
```bash
./mvnw spring-boot:run
```

A aplicação iniciará na porta `8080` (ex: `http://localhost:8080`).

---

## 📖 Documentação Interativa Swagger UI

A API possui documentação viva e interativa via **OpenAPI 3 / Swagger UI**.

- **URL de Acesso**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Como autenticar no Swagger UI:
1. Execute a requisição `POST /api/auth/register` ou `POST /api/auth/login` na própria interface do Swagger.
2. Copie o token retornado no campo `token`.
3. Clique no botão verde **Authorize** no canto superior direito da página do Swagger.
4. Cole o token no campo de texto e confirme. Todas as requisições enviadas pelo Swagger utilizarão o header `Authorization: Bearer <seu_token>` automaticamente.

---

## 📮 Coleção do Postman

Para testar via Postman, import o arquivo localizado na raiz do repositório:
- `Foody_Delivery_API.postman_collection.json`

Contém todas as requisições configuradas com a variável `{{baseUrl}}` e script automático que salva o token no login para preencher `{{token}}` de forma 100% automatizada.

---

## 🧪 Executando os Testes Automatizados

Para rodar os testes unitários da aplicação:

```powershell
.\mvnw.cmd test
```

---

## 📌 Tabela de Endpoints da API

| Método | Endpoint | Protegido? | Descrição |
| :--- | :--- | :---: | :--- |
| `POST` | `/api/auth/register` | ❌ Não | Cadastro de novo usuário |
| `POST` | `/api/auth/login` | ❌ Não | Autenticação e geração de token JWT |
| `POST` | `/api/orders` | 🔒 Sim | Criar novo pedido de delivery com itens |
| `GET` | `/api/orders` | 🔒 Sim | Listar todos os pedidos (ordenados do mais recente) |
| `GET` | `/api/orders/{id}` | 🔒 Sim | Buscar detalhes de um pedido específico por ID |
| `PATCH`| `/api/orders/{id}/status` | 🔒 Sim | Atualizar status do pedido (`RECEBIDO`, `EM_PREPARO`, `SAIU_PARA_ENTREGA`, `ENTREGUE`, `CANCELADO`) |
| `GET` | `/api/orders/{id}/history` | 🔒 Sim | Obter linha do tempo de auditoria do pedido |
| `PUT` | `/api/orders/{id}` | 🔒 Sim | Editar dados e itens de um pedido |
| `DELETE`| `/api/orders/{id}` | 🔒 Sim | Excluir um pedido do sistema |

---

## 🗄️ Console do Banco de Dados H2

Para visualizar os dados direto no banco durante a execução:
- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:file:./data/foody_delivery`
- **User**: `sa`
- **Password**: *(em branco)*
