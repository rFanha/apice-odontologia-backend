package com.ruifanha.clinicawisestart.dto.usuario;

import com.ruifanha.clinicawisestart.domain.usuario.PerfilUsuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Dados recebidos para criar ou atualizar usuarios.
public record UsuarioRequest(
	@NotBlank(message = "Nome e obrigatorio.")
	@Size(max = 255, message = "Nome deve ter no maximo 255 caracteres.")
	String nome,

	@NotBlank(message = "CPF e obrigatorio.")
	@Size(max = 14, message = "CPF deve ter no maximo 14 caracteres.")
	String cpf,

	@NotBlank(message = "Email e obrigatorio.")
	@Size(max = 255, message = "Email deve ter no maximo 255 caracteres.")
	String email,

	@Size(max = 255, message = "Senha deve ter no maximo 255 caracteres.")
	String senha,

	@NotNull(message = "Perfil e obrigatorio.")
	PerfilUsuario perfil,

	@NotNull(message = "Status ativo e obrigatorio.")
	Boolean ativo
) {
}
