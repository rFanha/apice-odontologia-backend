package com.ruifanha.clinicawisestart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ruifanha.clinicawisestart.dto.autenticacao.LoginRequest;
import com.ruifanha.clinicawisestart.dto.autenticacao.LoginResponse;
import com.ruifanha.clinicawisestart.service.AutenticacaoService;

// Controller criado para expor os endpoints iniciais de autenticacao.
@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

	private final AutenticacaoService autenticacaoService;

	public AutenticacaoController(AutenticacaoService autenticacaoService) {
		this.autenticacaoService = autenticacaoService;
	}

	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public LoginResponse login(@RequestBody LoginRequest loginRequest) {
		try {
			return autenticacaoService.autenticar(loginRequest);
		} catch (IllegalArgumentException exception) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, exception.getMessage(), exception);
		}
	}
}
