package com.ruifanha.clinicawisestart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ruifanha.clinicawisestart.domain.paciente.Paciente;

// Repository criado para centralizar o acesso ao banco dos pacientes.
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

	// Busca paciente pelo email, campo unico usado em validacoes de cadastro.
	Optional<Paciente> findByEmail(String email);

	// Busca paciente pelo CPF, campo unico usado para evitar cadastros duplicados.
	Optional<Paciente> findByCpf(String cpf);

	// Verifica duplicidade de email antes de criar ou atualizar pacientes.
	boolean existsByEmail(String email);

	// Verifica duplicidade de CPF antes de criar ou atualizar pacientes.
	boolean existsByCpf(String cpf);
}
