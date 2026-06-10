package com.ruifanha.clinicawisestart.dto.autenticacao;

import jakarta.validation.constraints.NotBlank;

// Dados recebidos no endpoint de login.
public record LoginRequest(
	@NotBlank(message = "Email e obrigatorio.")
	String email,

	@NotBlank(message = "Senha e obrigatoria.")
	String senha
) {
}
