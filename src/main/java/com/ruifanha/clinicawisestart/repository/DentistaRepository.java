package com.ruifanha.clinicawisestart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ruifanha.clinicawisestart.domain.dentista.Dentista;

// Repository criado para centralizar o acesso ao banco dos dentistas.
public interface DentistaRepository extends JpaRepository<Dentista, Long> {

	// Busca dentista pelo email, campo unico usado em validacoes de cadastro.
	Optional<Dentista> findByEmail(String email);

	// Busca dentista pelo CPF, campo unico usado para evitar cadastros duplicados.
	Optional<Dentista> findByCpf(String cpf);

	// Busca dentista pelo CRO, identificador profissional informado no cadastro.
	Optional<Dentista> findByCro(String cro);

	// Lista dentistas pelo status de ativo para apoiar filtros de agenda.
	List<Dentista> findByAtivo(Boolean ativo);

	// Verifica duplicidade de email antes de criar ou atualizar dentistas.
	boolean existsByEmail(String email);

	// Verifica duplicidade de CPF antes de criar ou atualizar dentistas.
	boolean existsByCpf(String cpf);

	// Verifica duplicidade de CRO antes de criar ou atualizar dentistas.
	boolean existsByCro(String cro);
}
