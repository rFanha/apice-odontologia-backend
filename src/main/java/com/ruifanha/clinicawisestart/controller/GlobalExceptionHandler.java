package com.ruifanha.clinicawisestart.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.ruifanha.clinicawisestart.dto.erro.ErroResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErroResponse> tratarErroValidacao(
		MethodArgumentNotValidException exception,
		HttpServletRequest request
	) {
		Map<String, String> campos = new LinkedHashMap<>();
		exception.getBindingResult().getFieldErrors().forEach(erro ->
			campos.put(erro.getField(), erro.getDefaultMessage())
		);
		exception.getBindingResult().getGlobalErrors().forEach(erro ->
			campos.put(erro.getObjectName(), erro.getDefaultMessage())
		);

		HttpStatus status = HttpStatus.BAD_REQUEST;
		return ResponseEntity.status(status).body(criarResposta(
			status,
			"Dados invalidos.",
			request.getRequestURI(),
			campos
		));
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ErroResponse> tratarResponseStatusException(
		ResponseStatusException exception,
		HttpServletRequest request
	) {
		HttpStatusCode statusCode = exception.getStatusCode();
		String mensagem = exception.getReason() != null ? exception.getReason() : "Erro ao processar requisicao.";

		return ResponseEntity.status(statusCode).body(criarResposta(
			statusCode,
			mensagem,
			request.getRequestURI(),
			Map.of()
		));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErroResponse> tratarIllegalArgumentException(
		IllegalArgumentException exception,
		HttpServletRequest request
	) {
		HttpStatus status = definirStatusRegraNegocio(exception.getMessage());
		return ResponseEntity.status(status).body(criarResposta(
			status,
			exception.getMessage(),
			request.getRequestURI(),
			Map.of()
		));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErroResponse> tratarAcessoNegado(
		AccessDeniedException exception,
		HttpServletRequest request
	) {
		HttpStatus status = HttpStatus.FORBIDDEN;
		return ResponseEntity.status(status).body(criarResposta(
			status,
			"Acesso negado.",
			request.getRequestURI(),
			Map.of()
		));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErroResponse> tratarErroInesperado(Exception exception, HttpServletRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		return ResponseEntity.status(status).body(criarResposta(
			status,
			"Erro interno do servidor.",
			request.getRequestURI(),
			Map.of()
		));
	}

	private ErroResponse criarResposta(
		HttpStatusCode statusCode,
		String mensagem,
		String caminho,
		Map<String, String> campos
	) {
		int status = statusCode.value();
		return new ErroResponse(
			LocalDateTime.now(),
			status,
			HttpStatus.valueOf(status).getReasonPhrase(),
			mensagem,
			caminho,
			campos
		);
	}

	private HttpStatus definirStatusRegraNegocio(String mensagem) {
		if (mensagem != null && mensagem.contains("nao encontrad")) {
			return HttpStatus.NOT_FOUND;
		}
		if (mensagem != null && (mensagem.contains("ADMIN") || mensagem.contains("DENTISTA"))) {
			return HttpStatus.FORBIDDEN;
		}
		return HttpStatus.BAD_REQUEST;
	}
}
