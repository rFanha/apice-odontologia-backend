package com.ruifanha.clinicawisestart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ruifanha.clinicawisestart.domain.usuario.Usuario;

// Repository criado para centralizar o acesso ao banco dos usuarios.
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	// Busca usuario pelo email, campo unico usado em login e validacoes.
	Optional<Usuario> findByEmail(String email);

	// Busca usuario pelo CPF, campo unico usado para evitar cadastros duplicados.
	Optional<Usuario> findByCpf(String cpf);

	// Verifica duplicidade de email antes de criar ou atualizar usuarios.
	boolean existsByEmail(String email);

	// Verifica duplicidade de CPF antes de criar ou atualizar usuarios.
	boolean existsByCpf(String cpf);
}
