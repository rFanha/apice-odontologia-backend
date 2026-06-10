package com.ruifanha.clinicawisestart.dto.usuario;

import com.ruifanha.clinicawisestart.domain.usuario.PerfilUsuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Dados recebidos para criar ou atualizar usuarios.
public record UsuarioRequest(
	@NotBlank(message = "Nome e obrigatorio.")
	String nome,

	@NotBlank(message = "CPF e obrigatorio.")
	String cpf,

	@NotBlank(message = "Email e obrigatorio.")
	String email,

	String senha,

	@NotNull(message = "Perfil e obrigatorio.")
	PerfilUsuario perfil,

	@NotNull(message = "Status ativo e obrigatorio.")
	Boolean ativo
) {
}
