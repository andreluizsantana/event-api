package com.project.EventAPI.dto.response;

import java.time.LocalDateTime;
import com.project.EventAPI.dto.EnderecoDTO;
import com.project.EventAPI.enums.Status;

public record EventResponseDTO(Long id, String titulo, String descricaoEvento, LocalDateTime previsaoInicio,
    LocalDateTime previsaoFim, Status status, EnderecoDTO endereco) {
}
