package com.project.EventAPI.dto.update;

import java.math.BigDecimal;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record ItemEventoUpdateDTO(
    @Size(max = 120, message = "A descrição deve ter no máximo 120 caracteres")
    @Pattern(regexp = ".*\\S.*", message = "A descrição não pode estar vazia") String descricao,
    @PositiveOrZero(message = "O valor não pode ser negativo") BigDecimal valor) {
}
