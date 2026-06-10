package com.ruifanha.clinicawisestart.validation;

import com.ruifanha.clinicawisestart.dto.consulta.ConsultaRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PeriodoConsultaValidator implements ConstraintValidator<PeriodoConsultaValido, ConsultaRequest> {

	@Override
	public boolean isValid(ConsultaRequest consultaRequest, ConstraintValidatorContext context) {
		if (consultaRequest == null || consultaRequest.dataInicio() == null || consultaRequest.dataFim() == null) {
			return true;
		}

		return consultaRequest.dataFim().isAfter(consultaRequest.dataInicio());
	}
}
