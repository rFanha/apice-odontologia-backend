package com.ruifanha.clinicawisestart.dto.usuario;

import com.ruifanha.clinicawisestart.domain.usuario.PerfilUsuario;

// Dados recebidos para criar ou atualizar usuarios.
public record UsuarioRequest(
	String nome,
	String cpf,
	String email,
	String senha,
	PerfilUsuario perfil,
	Boolean ativo
) {
}
