package com.ruifanha.clinicawisestart.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ruifanha.clinicawisestart.domain.consulta.Consulta;
import com.ruifanha.clinicawisestart.domain.consulta.StatusConsulta;

// Repository criado para centralizar o acesso ao banco das consultas.
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

	// Verifica se ja existe consulta ativa no mesmo intervalo para o dentista.
	@Query("""
		SELECT COUNT(c) > 0
		FROM Consulta c
		WHERE c.dentista.id = :dentistaId
		AND (:consultaId IS NULL OR c.id <> :consultaId)
		AND c.status <> :statusIgnorado
		AND :dataInicio < c.dataFim
		AND :dataFim > c.dataInicio
	""")
	boolean existsConflitoHorario(
		@Param("consultaId") Long consultaId,
		@Param("dentistaId") Long dentistaId,
		@Param("dataInicio") LocalDateTime dataInicio,
		@Param("dataFim") LocalDateTime dataFim,
		@Param("statusIgnorado") StatusConsulta statusIgnorado
	);
}
