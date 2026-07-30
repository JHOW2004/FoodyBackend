package com.foody.delivery.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta de autenticação contendo o token JWT e dados do usuário")
public class AuthResponse {

    @Schema(description = "Token de acesso JWT gerado", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String token;

    @Schema(description = "Tipo de token para uso no header Authorization", example = "Bearer")
    @Builder.Default
    private String tokenType = "Bearer";

    @Schema(description = "ID do usuário autenticado", example = "1")
    private Long userId;

    @Schema(description = "Nome do usuário", example = "Desenvolvedor Foody")
    private String name;

    @Schema(description = "E-mail do usuário", example = "dev@foody.com.br")
    private String email;

    @Schema(description = "Papel/Role do usuário", example = "USER")
    private String role;
}
