package com.ruifanha.clinicawisestart.dto.autenticacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

// Dados recebidos no endpoint de login.
public record LoginRequest(
	@NotBlank(message = "Email e obrigatorio.")
	@Email(message = "Email deve ter um formato valido.")
	@Size(max = 255, message = "Email deve ter no maximo 255 caracteres.")
	String email,

	@NotBlank(message = "Senha e obrigatoria.")
	@Size(max = 255, message = "Senha deve ter no maximo 255 caracteres.")
	String senha
) {
}
