package com.foody.delivery.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Credenciais para autenticação de usuário")
public class LoginRequest {

    @Schema(description = "E-mail cadastrado", example = "dev@foody.com.br")
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @Schema(description = "Senha cadastrada", example = "senha123")
    @NotBlank(message = "A senha é obrigatória")
    private String password;
}
