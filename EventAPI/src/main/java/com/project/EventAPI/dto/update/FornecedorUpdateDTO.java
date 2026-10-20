package com.project.EventAPI.dto.update;

import java.math.BigDecimal;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record FornecedorUpdateDTO(
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    @Pattern(regexp = ".*\\S.*", message = "O nome não pode estar vazio") String nome,
    @Size(max = 200, message = "O serviço deve ter no máximo 200 caracteres") String servico,
    @PositiveOrZero(message = "O valor do contrato não pode ser negativo") BigDecimal valorContrato) {
}
