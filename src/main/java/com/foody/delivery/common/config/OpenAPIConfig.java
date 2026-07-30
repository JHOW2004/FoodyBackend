package com.foody.delivery.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Foody Delivery - Mini Rastreador de Pedidos API")
                        .version("1.0.0")
                        .description("### Documentação Oficial da API REST - Processo Seletivo Foody Delivery\n\n" +
                                "Esta API REST foi desenvolvida em **Java 21 com Spring Boot 3** como parte do desafio técnico para Desenvolvedor Full-Stack Pleno.\n\n" +
                                "#### 🔑 Como Autenticar na Documentação Swagger UI:\n" +
                                "1. Utilize o endpoint `POST /api/auth/register` ou `POST /api/auth/login` para obter o seu token JWT.\n" +
                                "2. Copie a string do token retornado no campo `token` da resposta.\n" +
                                "3. Clique no botão **Authorize** (cadeado verde no topo direito desta página).\n" +
                                "4. Cole o token no campo de texto e clique em **Authorize**.\n" +
                                "5. Todas as requisições aos endpoints de `/api/orders` serão enviadas com o cabeçalho `Authorization: Bearer <seu_token>` automaticamente.\n\n" +
                                "#### 🚴 Fluxo de Status do Pedido:\n" +
                                "- **RECEBIDO**: Status inicial gerado automaticamente ao criar o pedido.\n" +
                                "- **EM_PREPARO**: O pedido foi aceito pela cozinha e está sendo produzido.\n" +
                                "- **SAIU_PARA_ENTREGA**: O entregador retirou o pedido e está a caminho do cliente.\n" +
                                "- **ENTREGUE**: O pedido foi entregue com sucesso (status final).\n" +
                                "- **CANCELADO**: O pedido foi cancelado (status final).\n\n" +
                                "*Nota: Pedidos com status finais (`ENTREGUE` ou `CANCELADO`) não aceitam novas alterações de status ou edição.*")
                        .contact(new Contact()
                                .name("Jonathan (Desenvolvedor Full-Stack)")
                                .email("jonathan@foody.com.br")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Insira o token de acesso JWT obtido no endpoint de login.")));
    }
}
