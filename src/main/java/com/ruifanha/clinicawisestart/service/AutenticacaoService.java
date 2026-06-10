package com.ruifanha.clinicawisestart.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruifanha.clinicawisestart.domain.usuario.Usuario;
import com.ruifanha.clinicawisestart.dto.autenticacao.LoginRequest;
import com.ruifanha.clinicawisestart.dto.autenticacao.LoginResponse;
import com.ruifanha.clinicawisestart.repository.UsuarioRepository;
import com.ruifanha.clinicawisestart.security.JwtService;

// Service criado para concentrar as regras iniciais de autenticacao.
@Service
public class AutenticacaoService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AutenticacaoService(
		UsuarioRepository usuarioRepository,
		PasswordEncoder passwordEncoder,
		JwtService jwtService
	) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
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

		String token = jwtService.gerarToken(usuario);

		return new LoginResponse(
			usuario.getId(),
			usuario.getNome(),
			usuario.getEmail(),
			usuario.getPerfil(),
			usuario.getAtivo(),
			token
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
		// Compara a senha informada com o hash BCrypt armazenado.
		if (!passwordEncoder.matches(senhaInformada, usuario.getSenha())) {
			throw new IllegalArgumentException("Email ou senha invalidos.");
		}
	}
}
