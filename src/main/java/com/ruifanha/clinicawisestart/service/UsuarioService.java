package com.ruifanha.clinicawisestart.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruifanha.clinicawisestart.domain.usuario.PerfilUsuario;
import com.ruifanha.clinicawisestart.domain.usuario.Usuario;
import com.ruifanha.clinicawisestart.dto.usuario.UsuarioRequest;
import com.ruifanha.clinicawisestart.repository.UsuarioRepository;

// Service criado para concentrar regras de permissao relacionadas aos usuarios.
@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public Usuario criar(Usuario usuarioLogado, UsuarioRequest usuarioRequest) {
		validarPermissaoGerenciarUsuarios(usuarioLogado);

		Usuario usuario = new Usuario();
		aplicarDados(usuario, usuarioRequest, true);
		validarDuplicidadeEmail(usuario);
		validarDuplicidadeCpf(usuario);

		return usuarioRepository.save(usuario);
	}

	@Transactional
	public Usuario atualizar(Usuario usuarioLogado, Long id, UsuarioRequest usuarioRequest) {
		validarPermissaoGerenciarUsuarios(usuarioLogado);

		Usuario usuario = buscarPorIdInterno(id);
		aplicarDados(usuario, usuarioRequest, false);
		validarDuplicidadeEmail(usuario);
		validarDuplicidadeCpf(usuario);

		return usuarioRepository.save(usuario);
	}

	@Transactional(readOnly = true)
	public List<Usuario> listarTodos(Usuario usuarioLogado) {
		validarPermissaoGerenciarUsuarios(usuarioLogado);
		return usuarioRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Usuario buscarPorId(Usuario usuarioLogado, Long id) {
		validarPermissaoGerenciarUsuarios(usuarioLogado);
		return buscarPorIdInterno(id);
	}

	@Transactional
	public void excluir(Usuario usuarioLogado, Long id) {
		validarPermissaoGerenciarUsuarios(usuarioLogado);

		// Confirma que o usuario existe antes de solicitar a exclusao.
		Usuario usuario = buscarPorIdInterno(id);
		usuarioRepository.delete(usuario);
	}

	public void validarPermissaoGerenciarUsuarios(Usuario usuarioLogado) {
		// Bloqueia perfis diferentes de ADMIN na gestao de usuarios.
		if (usuarioLogado == null || !PerfilUsuario.ADMIN.equals(usuarioLogado.getPerfil())) {
			throw new IllegalArgumentException("Somente usuarios ADMIN podem gerenciar usuarios.");
		}
	}

	private Usuario buscarPorIdInterno(Long id) {
		// Centraliza a busca por id para manter uma mensagem padronizada.
		return usuarioRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado."));
	}

	private void aplicarDados(Usuario usuario, UsuarioRequest usuarioRequest, boolean exigirSenha) {
		// Copia os dados recebidos para a entidade antes de salvar.
		if (usuarioRequest == null) {
			throw new IllegalArgumentException("Dados do usuario sao obrigatorios.");
		}

		usuario.setNome(usuarioRequest.nome());
		usuario.setCpf(usuarioRequest.cpf());
		usuario.setEmail(usuarioRequest.email());
		usuario.setPerfil(usuarioRequest.perfil());
		usuario.setAtivo(usuarioRequest.ativo());

		if (usuarioRequest.senha() != null && !usuarioRequest.senha().isBlank()) {
			usuario.setSenha(passwordEncoder.encode(usuarioRequest.senha()));
		} else if (exigirSenha) {
			throw new IllegalArgumentException("Senha e obrigatoria.");
		}
	}

	private void validarDuplicidadeEmail(Usuario usuario) {
		// Evita cadastro de usuarios com email ja utilizado por outro registro.
		usuarioRepository.findByEmail(usuario.getEmail())
			.filter(usuarioExistente -> !usuarioExistente.getId().equals(usuario.getId()))
			.ifPresent(usuarioExistente -> {
				throw new IllegalArgumentException("Ja existe usuario cadastrado com este email.");
			});
	}

	private void validarDuplicidadeCpf(Usuario usuario) {
		// Evita cadastro de usuarios com CPF ja utilizado por outro registro.
		usuarioRepository.findByCpf(usuario.getCpf())
			.filter(usuarioExistente -> !usuarioExistente.getId().equals(usuario.getId()))
			.ifPresent(usuarioExistente -> {
				throw new IllegalArgumentException("Ja existe usuario cadastrado com este CPF.");
			});
	}
}
