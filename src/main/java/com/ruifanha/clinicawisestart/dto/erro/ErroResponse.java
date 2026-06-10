package com.ruifanha.clinicawisestart.dto.erro;

import java.time.LocalDateTime;
import java.util.Map;

public record ErroResponse(
	LocalDateTime timestamp,
	int status,
	String erro,
	String mensagem,
	String caminho,
	Map<String, String> campos
) {
}
