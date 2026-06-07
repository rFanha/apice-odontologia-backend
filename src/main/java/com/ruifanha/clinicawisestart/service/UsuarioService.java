package com.ruifanha.clinicawisestart.service;

import org.springframework.stereotype.Service;

import com.ruifanha.clinicawisestart.domain.usuario.PerfilUsuario;
import com.ruifanha.clinicawisestart.domain.usuario.Usuario;

// Service criado para concentrar regras de permissao relacionadas aos usuarios.
@Service
public class UsuarioService {

	public void validarPermissaoGerenciarUsuarios(Usuario usuarioLogado) {
		// Bloqueia perfis diferentes de ADMIN na gestao de usuarios.
		if (usuarioLogado == null || !PerfilUsuario.ADMIN.equals(usuarioLogado.getPerfil())) {
			throw new IllegalArgumentException("Somente usuarios ADMIN podem gerenciar usuarios.");
		}
	}
}
