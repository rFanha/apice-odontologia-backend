package com.ruifanha.clinicawisestart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ruifanha.clinicawisestart.domain.usuario.Usuario;
import com.ruifanha.clinicawisestart.dto.usuario.UsuarioRequest;
import com.ruifanha.clinicawisestart.dto.usuario.UsuarioResponse;
import com.ruifanha.clinicawisestart.service.UsuarioService;

import jakarta.validation.Valid;

// Controller criado para expor endpoints de usuarios protegidos por perfil ADMIN.
@RestController
@RequestMapping("/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@GetMapping
	public List<UsuarioResponse> listarTodos(@AuthenticationPrincipal Usuario usuarioLogado) {
		try {
			return usuarioService.listarTodos(usuarioLogado)
				.stream()
				.map(UsuarioResponse::fromEntity)
				.toList();
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	@GetMapping("/{id}")
	public UsuarioResponse buscarPorId(
		@AuthenticationPrincipal Usuario usuarioLogado,
		@PathVariable Long id
	) {
		try {
			return UsuarioResponse.fromEntity(usuarioService.buscarPorId(usuarioLogado, id));
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UsuarioResponse criar(
		@AuthenticationPrincipal Usuario usuarioLogado,
		@Valid @RequestBody UsuarioRequest usuarioRequest
	) {
		try {
			return UsuarioResponse.fromEntity(usuarioService.criar(usuarioLogado, usuarioRequest));
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	@PutMapping("/{id}")
	public UsuarioResponse atualizar(
		@AuthenticationPrincipal Usuario usuarioLogado,
		@PathVariable Long id,
		@Valid @RequestBody UsuarioRequest usuarioRequest
	) {
		try {
			return UsuarioResponse.fromEntity(usuarioService.atualizar(usuarioLogado, id, usuarioRequest));
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(
		@AuthenticationPrincipal Usuario usuarioLogado,
		@PathVariable Long id
	) {
		try {
			usuarioService.excluir(usuarioLogado, id);
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	private ResponseStatusException converterErro(IllegalArgumentException exception) {
		// Converte erros de regra de negocio para respostas HTTP simples.
		if (exception.getMessage().contains("ADMIN")) {
			return new ResponseStatusException(HttpStatus.FORBIDDEN, exception.getMessage(), exception);
		}
		if (exception.getMessage().contains("nao encontrado")) {
			return new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
		}
		return new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
	}
}
