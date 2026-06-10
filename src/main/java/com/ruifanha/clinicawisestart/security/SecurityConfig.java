package com.ruifanha.clinicawisestart.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ruifanha.clinicawisestart.repository.UsuarioRepository;

// Configuracao central do Spring Security para JWT e autorizacao por perfil.
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(
		HttpSecurity http,
		JwtAuthenticationFilter jwtAuthenticationFilter
	) throws Exception {
		return http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
				.requestMatchers("/usuarios/**").hasRole("ADMIN")
				.anyRequest().authenticated()
			)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		// Usa BCrypt para armazenar e comparar senhas com hash seguro.
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
		// Evita usuario padrao do Spring e carrega credenciais reais do banco.
		return email -> usuarioRepository.findByEmail(email)
			.map(usuario -> User.withUsername(usuario.getEmail())
				.password(usuario.getSenha())
				.roles(usuario.getPerfil().name())
				.disabled(!Boolean.TRUE.equals(usuario.getAtivo()))
				.build())
			.orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado."));
	}
}
