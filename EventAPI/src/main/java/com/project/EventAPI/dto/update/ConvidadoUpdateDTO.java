package com.project.EventAPI.dto.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record ConvidadoUpdateDTO(
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres") String nome,
    @Email(message = "E-mail inválido") @Size(max = 200, message = "O e-mail deve ter no máximo 200 caracteres") String email,
    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres") String telefone,
    Boolean confirmado) {
}
