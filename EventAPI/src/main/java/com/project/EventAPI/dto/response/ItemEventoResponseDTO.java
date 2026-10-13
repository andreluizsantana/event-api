package com.project.EventAPI.dto.response;

import java.math.BigDecimal;

public record ItemEventoResponseDTO(Long id, String descricao, BigDecimal valor, Long eventoId) {
}
