package com.project.EventAPI.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ItemEventoRequestDTO(
    @NotBlank(message = "A descrição é obrigatória") @Size(max = 120, message = "A descrição deve ter no máximo 120 caracteres") String descricao,
    @PositiveOrZero(message = "O valor não pode ser negativo") BigDecimal valor) {
}
