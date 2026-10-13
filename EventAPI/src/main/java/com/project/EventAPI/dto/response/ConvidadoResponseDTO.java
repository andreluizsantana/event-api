package com.project.EventAPI.dto.response;

public record ConvidadoResponseDTO(Long id, String nome, String email, String telefone, Boolean confirmado,
    Long eventoId) {
}
