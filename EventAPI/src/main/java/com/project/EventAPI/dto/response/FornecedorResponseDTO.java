package com.project.EventAPI.dto.response;

import java.math.BigDecimal;

public record FornecedorResponseDTO(Long id, String nome, String servico, BigDecimal valorContrato, Long eventoId) {
}
