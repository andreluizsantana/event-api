package com.project.EventAPI.dto.request;

import java.time.LocalDateTime;
import com.project.EventAPI.dto.EnderecoDTO;
import com.project.EventAPI.enums.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventRequestDTO(@NotBlank(message = "O título não pode estar vazio") String titulo,
    @NotBlank(message = "A descrição não pode estar vazia") String descricaoEvento,
    @NotNull(message = "A data de início é obrigatória") LocalDateTime previsaoInicio,
    @NotNull(message = "A data de fim é obrigatória") LocalDateTime previsaoFim, Status status,
    @Valid EnderecoDTO endereco) {
}
