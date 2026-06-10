package com.ruifanha.clinicawisestart.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

// Valida a consistencia entre data inicial e data final de uma consulta.
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = PeriodoConsultaValidator.class)
public @interface PeriodoConsultaValido {

	String message() default "Data final da consulta deve ser posterior a data inicial.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
