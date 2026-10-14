package com.project.EventAPI.dto;

import jakarta.validation.constraints.Size;

public record EnderecoDTO(
    @Size(max = 2, message = "UF deve ter no máximo 2 caracteres") String uf,
    @Size(max = 120, message = "Cidade deve ter no máximo 120 caracteres") String cidade,
    @Size(max = 200, message = "Rua deve ter no máximo 200 caracteres") String rua,
    @Size(max = 10, message = "Número deve ter no máximo 10 caracteres") String numero,
    @Size(max = 9, message = "CEP deve ter no máximo 9 caracteres") String cep,
    @Size(max = 120, message = "Referência deve ter no máximo 120 caracteres") String referencia) {
}
