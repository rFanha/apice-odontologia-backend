package com.ruifanha.clinicawisestart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ruifanha.clinicawisestart.domain.lead.Lead;

public interface LeadRepository extends JpaRepository<Lead, Long> {

	List<Lead> findByLidoOrderByDataCriacaoDesc(Boolean lido);

	List<Lead> findAllByOrderByDataCriacaoDesc();
}
