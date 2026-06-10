package com.ruifanha.clinicawisestart.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ruifanha.clinicawisestart.domain.usuario.Usuario;
import com.ruifanha.clinicawisestart.repository.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Filtro criado para autenticar requisicoes com token JWT no header Authorization.
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UsuarioRepository usuarioRepository;

	public JwtAuthenticationFilter(JwtService jwtService, UsuarioRepository usuarioRepository) {
		this.jwtService = jwtService;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String token = extrairToken(request);

		try {
			if (token != null && jwtService.tokenValido(token)) {
				String email = jwtService.extrairEmail(token);
				usuarioRepository.findByEmail(email)
					.filter(usuario -> Boolean.TRUE.equals(usuario.getAtivo()))
					.ifPresent(this::autenticar);
			}
		} catch (IllegalArgumentException exception) {
			SecurityContextHolder.clearContext();
		}

		filterChain.doFilter(request, response);
	}

	private String extrairToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return null;
		}
		return authorization.substring(7);
	}

	private void autenticar(Usuario usuario) {
		// Registra o usuario autenticado para as regras de autorizacao da requisicao.
		List<SimpleGrantedAuthority> authorities = List.of(
			new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name())
		);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			usuario,
			null,
			authorities
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
