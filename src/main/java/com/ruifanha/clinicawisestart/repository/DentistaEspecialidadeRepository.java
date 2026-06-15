package com.ruifanha.clinicawisestart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ruifanha.clinicawisestart.domain.dentista.Dentista;
import com.ruifanha.clinicawisestart.domain.dentista.DentistaEspecialidade;

public interface DentistaEspecialidadeRepository extends JpaRepository<DentistaEspecialidade, Long> {

	@Transactional
	void deleteByDentista(Dentista dentista);
}
