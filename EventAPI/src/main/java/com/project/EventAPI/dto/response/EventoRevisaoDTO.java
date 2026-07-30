package com.project.EventAPI.dto.response;

import java.time.Instant;

public record EventoRevisaoDTO(Long idRevisao, Instant timestamp, String usuario, String operacao,
    EventResponseDTO dados) {
}
