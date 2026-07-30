# Foody Delivery - Backend (Spring Boot 3 REST API)

API RESTful completa de alta performance desenvolvida em Java 21 com Spring Boot 3.3.4 para o sistema de gestão e rastreamento de pedidos de delivery da **Foody Delivery**.

---

## 🛠️ Tecnologias e Dependências

- **Linguagem & Runtime**: Java 21 (LTS)
- **Framework**: Spring Boot 3.3.4
- **Segurança & Autenticação**: Spring Security 6 + JWT (JSON Web Tokens com algoritmo HMAC-SHA512)
- **Persistência de Dados**: Spring Data JPA / Hibernate ORM
- **Banco de Dados**: H2 Embedded Database (Persistência em arquivo local `./data/foody_delivery.mv.db`)
- **Documentação de API Interativa**: Springdoc OpenAPI 3 / Swagger UI (`/swagger-ui.html`)
- **Inicialização de Dados**: `DataInitializer` (`CommandLineRunner`) para pré-cadastro de cardápio com 13 itens sem duplicação
- **Validação de DTOs**: Jakarta Validation (`@NotNull`, `@NotBlank`, `@Positive`)
- **Testes Automatizados**: JUnit 5 + Mockito (12 testes unitários passando 100%)

---

## 🏗️ Estrutura de Pacotes do Projeto

```text
com.foody.delivery
 ├── FoodyDeliveryApplication.java    # Classe Principal de Boot
 ├── common                           # Transversal (Segurança, Swagger, Exceções Globais)
 │   ├── config                       # SecurityConfig, OpenAPIConfig, DataInitializer
 │   └── exception                    # GlobalExceptionHandler, CustomExceptions, ErrorResponse
 ├── domain                           # Entidades JPA e Repositórios
 │   ├── model                        # User, Order, OrderItem, OrderStatusHistory, Product
 │   └── repository                   # UserRepository, OrderRepository, OrderStatusHistoryRepository, ProductRepository
 ├── security                         # Módulo de Autenticação JWT
 │   ├── controller                   # AuthController (/api/auth)
 │   ├── dto                          # LoginRequest, RegisterRequest, AuthResponse
 │   ├── jwt                          # JwtService, JwtAuthenticationFilter
 │   └── service                      # AuthService, CustomUserDetailsService
 ├── product                          # Módulo de Gestão do Cardápio de Produtos
 │   ├── controller                   # ProductController (/api/products)
 │   ├── dto                          # ProductRequest, ProductResponse
 │   ├── model                        # ProductCategory Enum
 │   └── service                      # ProductService
 └── order                            # Módulo de Gestão de Pedidos
     ├── controller                   # OrderController (/api/orders)
     ├── dto                          # CreateOrderRequest, OrderResponse, UpdateOrderStatusRequest...
     └── service                      # OrderService
```

---

## 🚀 Como Executar o Backend Localmente

### Pré-requisitos
- **Java 21** instalado (`java -version`)
- O Maven Wrapper (`mvnw.cmd` no Windows / `./mvnw` no Linux/macOS) já está incluso na raiz.

### Passo a Passo:

1. **Acessar a pasta do backend**:
   ```bash
   cd C:\Projetos\Foody\backend
   ```

2. **Executar a aplicação via Maven**:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   *O backend estará rodando na porta **`8080`** (`http://localhost:8080`).*

3. **Verificar os Testes Unitários**:
   ```powershell
   .\mvnw.cmd test
   ```

---

## 📑 Documentação dos Endpoints REST

### 📄 Swagger UI (Interativo)
Acesse no navegador: **`http://localhost:8080/swagger-ui.html`**

Você poderá testar todas as rotas diretamente pela interface, incluindo o botão de autorização **`Authorize 🔓`** para passar o token JWT Bearer.

---

### 📦 Coleção do Postman
O repositório contém o arquivo **`Foody_Delivery_API.postman_collection.json`** configurado com scripts pós-login que salvam automaticamente o token JWT nas variáveis locais.

#### Principais Endpoints:

#### 🟢 Autenticação (`/api/auth`)
- `POST /api/auth/register`: Cadastro de usuário
- `POST /api/auth/login`: Autenticação e geração do Bearer JWT

#### 🍕 Produtos e Cardápio (`/api/products`)
- `GET /api/products`: Lista todos os produtos do cardápio
- `GET /api/products/category/{category}`: Filtra produtos por categoria (`HAMBURGUER`, `CACHORRO_QUENTE`, `SANDUICHE`, `BEBIDA`, `SOBREMESA`)
- `GET /api/products/{id}`: Detalhes do produto
- `POST /api/products`: Cadastra novo produto no cardápio
- `PUT /api/products/{id}`: Edita dados de um produto
- `DELETE /api/products/{id}`: Exclui um produto do cardápio

#### 🛵 Pedidos (`/api/orders`)
- `POST /api/orders`: Cria um novo pedido de delivery
- `GET /api/orders`: Lista todos os pedidos ordenados por data
- `GET /api/orders/{id}`: Busca detalhes de um pedido
- `PATCH /api/orders/{id}/status`: Atualiza o status (`RECEBIDO` ➔ `EM_PREPARO` ➔ `SAIU_PARA_ENTREGA` ➔ `ENTREGUE` ou `CANCELADO`)
- `GET /api/orders/{id}/history`: Histórico de auditoria e linha do tempo do pedido
- `PUT /api/orders/{id}`: Edita dados e itens do pedido
- `DELETE /api/orders/{id}`: Exclui um pedido
