package com.ruifanha.clinicawisestart.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ruifanha.clinicawisestart.domain.usuario.Usuario;

// Service criado para gerar e validar tokens JWT assinados pela API.
@Service
public class JwtService {

	private final String secret;
	private final long expirationMinutes;

	public JwtService(
		@Value("${api.security.jwt.secret}") String secret,
		@Value("${api.security.jwt.expiration-minutes}") long expirationMinutes
	) {
		this.secret = secret;
		this.expirationMinutes = expirationMinutes;
	}

	public String gerarToken(Usuario usuario) {
		long expiracao = Instant.now().plusSeconds(expirationMinutes * 60).getEpochSecond();
		String header = base64Url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
		String payload = base64Url("""
			{"sub":"%s","perfil":"%s","exp":%d}
			""".formatted(usuario.getEmail(), usuario.getPerfil().name(), expiracao).trim());
		String assinatura = assinar(header + "." + payload);
		return header + "." + payload + "." + assinatura;
	}

	public String extrairEmail(String token) {
		String payload = decodificarPayload(token);
		return extrairTexto(payload, "\"sub\":\"", "\"");
	}

	public boolean tokenValido(String token) {
		String[] partes = token.split("\\.");
		if (partes.length != 3) {
			return false;
		}

		String assinaturaEsperada = assinar(partes[0] + "." + partes[1]);
		if (!assinaturaEsperada.equals(partes[2])) {
			return false;
		}

		String payload = decodificarPayload(token);
		long expiracao = Long.parseLong(extrairTexto(payload, "\"exp\":", "}"));
		return Instant.now().getEpochSecond() < expiracao;
	}

	private String decodificarPayload(String token) {
		String[] partes = token.split("\\.");
		if (partes.length != 3) {
			throw new IllegalArgumentException("Token JWT invalido.");
		}
		return new String(Base64.getUrlDecoder().decode(partes[1]), StandardCharsets.UTF_8);
	}

	private String extrairTexto(String texto, String inicio, String fim) {
		int indiceInicio = texto.indexOf(inicio);
		if (indiceInicio < 0) {
			throw new IllegalArgumentException("Token JWT invalido.");
		}

		int valorInicio = indiceInicio + inicio.length();
		int valorFim = texto.indexOf(fim, valorInicio);
		if (valorFim < 0) {
			throw new IllegalArgumentException("Token JWT invalido.");
		}

		return texto.substring(valorInicio, valorFim);
	}

	private String base64Url(String valor) {
		return Base64.getUrlEncoder()
			.withoutPadding()
			.encodeToString(valor.getBytes(StandardCharsets.UTF_8));
	}

	private String assinar(String valor) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec chave = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
			mac.init(chave);
			return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(valor.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception exception) {
			throw new IllegalStateException("Erro ao assinar JWT.", exception);
		}
	}
}
