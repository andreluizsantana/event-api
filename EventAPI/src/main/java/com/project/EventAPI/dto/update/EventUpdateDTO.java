package com.project.EventAPI.dto.update;

import java.time.LocalDateTime;
import com.project.EventAPI.dto.EnderecoDTO;
import com.project.EventAPI.enums.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

public record EventUpdateDTO(
    @Size(max = 120, message = "O título deve ter no máximo 120 caracteres") String titulo,
    String descricaoEvento,
    LocalDateTime previsaoInicio,
    LocalDateTime previsaoFim,
    Status status,
    @Valid EnderecoDTO endereco) {
}
