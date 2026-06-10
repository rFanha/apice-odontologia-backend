package com.ruifanha.clinicawisestart.dto.autenticacao;

import com.ruifanha.clinicawisestart.domain.usuario.PerfilUsuario;

// Dados retornados apos autenticacao, sem expor a senha do usuario.
public record LoginResponse(
	Long id,
	String nome,
	String email,
	PerfilUsuario perfil,
	Boolean ativo,
	String token
) {
}
