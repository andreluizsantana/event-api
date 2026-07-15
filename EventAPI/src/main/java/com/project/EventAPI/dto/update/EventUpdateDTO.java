package com.project.EventAPI.dto.update;

import java.time.LocalDateTime;
import com.project.EventAPI.entity.Endereco;
import com.project.EventAPI.enums.Status;

public record EventUpdateDTO(
        String titulo,
        String descricaoEvento,
        LocalDateTime previsaoInicio,
        LocalDateTime previsaoFim,
        Status status,
        Endereco endereco
) {
}
