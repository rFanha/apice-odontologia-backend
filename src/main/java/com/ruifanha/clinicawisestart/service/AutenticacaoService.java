package com.ruifanha.clinicawisestart.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruifanha.clinicawisestart.domain.usuario.Usuario;
import com.ruifanha.clinicawisestart.dto.autenticacao.LoginRequest;
import com.ruifanha.clinicawisestart.dto.autenticacao.LoginResponse;
import com.ruifanha.clinicawisestart.repository.UsuarioRepository;

// Service criado para concentrar as regras iniciais de autenticacao.
@Service
public class AutenticacaoService {

	private final UsuarioRepository usuarioRepository;

	public AutenticacaoService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Transactional
	public LoginResponse autenticar(LoginRequest loginRequest) {
		validarDadosObrigatorios(loginRequest);

		Usuario usuario = usuarioRepository.findByEmail(loginRequest.email())
			.orElseThrow(() -> new IllegalArgumentException("Email ou senha invalidos."));

		validarUsuarioAtivo(usuario);
		validarSenha(loginRequest.senha(), usuario);

		// Registra o ultimo acesso bem-sucedido do usuario.
		usuario.setUltimoLogin(LocalDateTime.now());
		usuarioRepository.save(usuario);

		return new LoginResponse(
			usuario.getId(),
			usuario.getNome(),
			usuario.getEmail(),
			usuario.getPerfil(),
			usuario.getAtivo()
		);
	}

	private void validarDadosObrigatorios(LoginRequest loginRequest) {
		// Garante que a tentativa de login tenha os dados minimos.
		if (loginRequest == null
			|| loginRequest.email() == null
			|| loginRequest.email().isBlank()
			|| loginRequest.senha() == null
			|| loginRequest.senha().isBlank()) {
			throw new IllegalArgumentException("Email e senha sao obrigatorios.");
		}
	}

	private void validarUsuarioAtivo(Usuario usuario) {
		// Bloqueia login de usuarios desativados.
		if (!Boolean.TRUE.equals(usuario.getAtivo())) {
			throw new IllegalArgumentException("Usuario inativo.");
		}
	}

	private void validarSenha(String senhaInformada, Usuario usuario) {
		// Compara a senha atual; BCrypt/JWT entram nos itens de seguranca posteriores.
		if (!senhaInformada.equals(usuario.getSenha())) {
			throw new IllegalArgumentException("Email ou senha invalidos.");
		}
	}
}
