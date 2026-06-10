package com.ruifanha.clinicawisestart.dto.usuario;

import java.time.LocalDateTime;

import com.ruifanha.clinicawisestart.domain.usuario.PerfilUsuario;
import com.ruifanha.clinicawisestart.domain.usuario.Usuario;

// Dados retornados nos endpoints de usuarios, sem expor a senha.
public record UsuarioResponse(
	Long id,
	String nome,
	String cpf,
	String email,
	PerfilUsuario perfil,
	Boolean ativo,
	LocalDateTime dataCriacao,
	LocalDateTime ultimoLogin
) {

	public static UsuarioResponse fromEntity(Usuario usuario) {
		return new UsuarioResponse(
			usuario.getId(),
			usuario.getNome(),
			usuario.getCpf(),
			usuario.getEmail(),
			usuario.getPerfil(),
			usuario.getAtivo(),
			usuario.getDataCriacao(),
			usuario.getUltimoLogin()
		);
	}
}
