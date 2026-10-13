package com.project.EventAPI.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record FornecedorRequestDTO(
    @NotBlank(message = "O nome é obrigatório") @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres") String nome,
    @Size(max = 200, message = "O serviço deve ter no máximo 200 caracteres") String servico,
    @PositiveOrZero(message = "O valor do contrato não pode ser negativo") BigDecimal valorContrato) {
}
