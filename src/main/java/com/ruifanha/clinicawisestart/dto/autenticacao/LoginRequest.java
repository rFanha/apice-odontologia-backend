package com.ruifanha.clinicawisestart.dto.autenticacao;

// Dados recebidos no endpoint de login.
public record LoginRequest(
	String email,
	String senha
) {
}
