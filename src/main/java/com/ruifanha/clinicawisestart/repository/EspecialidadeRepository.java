package com.ruifanha.clinicawisestart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ruifanha.clinicawisestart.domain.especialidade.Especialidade;

// Repository criado para centralizar o acesso ao banco das especialidades.
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {

	// Busca especialidade pelo nome exato para apoiar validacoes de cadastro.
	Optional<Especialidade> findByNome(String nome);

	// Busca especialidades por parte do nome, ignorando maiusculas e minusculas.
	List<Especialidade> findByNomeContainingIgnoreCase(String nome);

	// Verifica se ja existe especialidade com o mesmo nome.
	boolean existsByNome(String nome);
}
