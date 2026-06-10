package com.ruifanha.clinicawisestart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ruifanha.clinicawisestart.domain.usuario.Usuario;
import com.ruifanha.clinicawisestart.dto.consulta.CancelamentoConsultaRequest;
import com.ruifanha.clinicawisestart.dto.consulta.ConsultaRequest;
import com.ruifanha.clinicawisestart.dto.consulta.ConsultaResponse;
import com.ruifanha.clinicawisestart.service.ConsultaService;

// Controller criado para expor o CRUD de consultas.
@RestController
@RequestMapping("/consultas")
public class ConsultaController {

	private final ConsultaService consultaService;

	public ConsultaController(ConsultaService consultaService) {
		this.consultaService = consultaService;
	}

	@GetMapping
	public List<ConsultaResponse> listar(
		@AuthenticationPrincipal Usuario usuarioLogado,
		@RequestParam(required = false) Long dentistaId
	) {
		try {
			return consultaService.listar(usuarioLogado, dentistaId)
				.stream()
				.map(ConsultaResponse::fromEntity)
				.toList();
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	@GetMapping("/{id}")
	public ConsultaResponse buscarPorId(@PathVariable Long id) {
		try {
			return ConsultaResponse.fromEntity(consultaService.buscarPorId(id));
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ConsultaResponse criar(
		@AuthenticationPrincipal Usuario usuarioLogado,
		@RequestBody ConsultaRequest consultaRequest
	) {
		try {
			return ConsultaResponse.fromEntity(consultaService.criar(usuarioLogado, consultaRequest));
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	@PutMapping("/{id}")
	public ConsultaResponse atualizar(
		@AuthenticationPrincipal Usuario usuarioLogado,
		@PathVariable Long id,
		@RequestBody ConsultaRequest consultaRequest
	) {
		try {
			return ConsultaResponse.fromEntity(consultaService.atualizar(usuarioLogado, id, consultaRequest));
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long id) {
		try {
			consultaService.excluir(id);
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	@PutMapping("/{id}/cancelar")
	public ConsultaResponse cancelar(
		@PathVariable Long id,
		@RequestBody CancelamentoConsultaRequest cancelamentoRequest
	) {
		try {
			return ConsultaResponse.fromEntity(consultaService.cancelar(id, cancelamentoRequest));
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	private ResponseStatusException converterErro(IllegalArgumentException exception) {
		// Converte erros de regra de negocio para respostas HTTP simples.
		if (exception.getMessage().contains("ADMIN")) {
			return new ResponseStatusException(HttpStatus.FORBIDDEN, exception.getMessage(), exception);
		}
		if (exception.getMessage().contains("nao encontrad")) {
			return new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
		}
		return new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
	}
}
