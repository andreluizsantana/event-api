package com.project.EventAPI.dto.request;



import java.time.LocalDateTime;
import com.project.EventAPI.entity.Endereco;
import com.project.EventAPI.enums.Status;
import jakarta.validation.constraints.NotBlank;

public record EventRequestDTO(@NotBlank(message = "O título não pode estar vazio") String titulo,
    @NotBlank(message = "A descrição não pode estar vazia") String descricaoEvento, LocalDateTime previsaoInicio,
    LocalDateTime previsaoFim, Status status, Endereco endereco) {
}
