package com.project.EventAPI.dto.update;

import java.time.LocalDateTime;
import com.project.EventAPI.dto.EnderecoDTO;
import com.project.EventAPI.enums.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record EventUpdateDTO(
    @Size(max = 120, message = "O título deve ter no máximo 120 caracteres")
    @Pattern(regexp = ".*\\S.*", message = "O título não pode estar vazio") String titulo,
    @Pattern(regexp = ".*\\S.*", message = "A descrição não pode estar vazia") String descricaoEvento,
    LocalDateTime previsaoInicio,
    LocalDateTime previsaoFim,
    Status status,
    @Valid EnderecoDTO endereco) {
}
