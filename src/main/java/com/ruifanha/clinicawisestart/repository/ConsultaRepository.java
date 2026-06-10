package com.ruifanha.clinicawisestart.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ruifanha.clinicawisestart.domain.consulta.Consulta;
import com.ruifanha.clinicawisestart.domain.consulta.StatusConsulta;

// Repository criado para centralizar o acesso ao banco das consultas.
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

	// Busca somente as consultas vinculadas ao dentista informado.
	List<Consulta> findByDentistaId(Long dentistaId);

	// Conta consultas por status para montar indicadores do dashboard.
	long countByStatus(StatusConsulta status);

	// Conta consultas aplicando filtros opcionais de relatorio.
	@Query("""
		SELECT COUNT(DISTINCT c)
		FROM Consulta c
		LEFT JOIN c.dentista.especialidades de
		WHERE (:usuarioId IS NULL OR c.usuario.id = :usuarioId)
		AND (:pacienteId IS NULL OR c.paciente.id = :pacienteId)
		AND (:especialidadeId IS NULL OR de.especialidade.id = :especialidadeId)
		AND (CAST(:dataInicio AS java.time.LocalDateTime) IS NULL OR c.dataInicio >= :dataInicio)
		AND (CAST(:dataFim AS java.time.LocalDateTime) IS NULL OR c.dataInicio <= :dataFim)
	""")
	long countRelatorio(
		@Param("usuarioId") Long usuarioId,
		@Param("pacienteId") Long pacienteId,
		@Param("especialidadeId") Long especialidadeId,
		@Param("dataInicio") LocalDateTime dataInicio,
		@Param("dataFim") LocalDateTime dataFim
	);

	// Conta consultas por status aplicando filtros opcionais de relatorio.
	@Query("""
		SELECT COUNT(DISTINCT c)
		FROM Consulta c
		LEFT JOIN c.dentista.especialidades de
		WHERE c.status = :status
		AND (:usuarioId IS NULL OR c.usuario.id = :usuarioId)
		AND (:pacienteId IS NULL OR c.paciente.id = :pacienteId)
		AND (:especialidadeId IS NULL OR de.especialidade.id = :especialidadeId)
		AND (CAST(:dataInicio AS java.time.LocalDateTime) IS NULL OR c.dataInicio >= :dataInicio)
		AND (CAST(:dataFim AS java.time.LocalDateTime) IS NULL OR c.dataInicio <= :dataFim)
	""")
	long countRelatorioPorStatus(
		@Param("usuarioId") Long usuarioId,
		@Param("pacienteId") Long pacienteId,
		@Param("especialidadeId") Long especialidadeId,
		@Param("dataInicio") LocalDateTime dataInicio,
		@Param("dataFim") LocalDateTime dataFim,
		@Param("status") StatusConsulta status
	);

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
