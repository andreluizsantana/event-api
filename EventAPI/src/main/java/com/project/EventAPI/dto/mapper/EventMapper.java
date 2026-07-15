package com.project.EventAPI.dto.mapper;

import com.project.EventAPI.dto.update.EventUpdateDTO;
import org.springframework.stereotype.Component;
import com.project.EventAPI.dto.request.EventRequestDTO;
import com.project.EventAPI.dto.response.EventResponseDTO;
import com.project.EventAPI.entity.Evento;

@Component
public class EventMapper {

    public EventResponseDTO entityToDto(Evento evento) {
        EventResponseDTO eventoresponsedto =
                new EventResponseDTO(evento.getId(), evento.getTitulo(), evento.getDescricaoEvento(),
                        evento.getPrevisaoInicio(), evento.getPrevisaoFim(), evento.getStatus(), evento.getEndereco());
        return eventoresponsedto;
    }

    public Evento dtoToEntity(EventRequestDTO eventrequestdto) {
        if (eventrequestdto == null) {
            throw new IllegalArgumentException("Event não pode ser nulo");
        }
        Evento evento = new Evento();
        evento.setTitulo(eventrequestdto.titulo());
        evento.setDescricaoEvento(eventrequestdto.descricaoEvento());
        evento.setPrevisaoInicio(eventrequestdto.previsaoInicio());
        evento.setPrevisaoFim(eventrequestdto.previsaoFim());
        evento.setStatus(eventrequestdto.status());
        evento.setEndereco(eventrequestdto.endereco());
        return evento;
    }

    public void updateEntityFromDto(EventUpdateDTO dto, Evento evento) {
        if (dto == null || evento == null) {
            return;
        }

        if (dto.titulo() != null) {
            evento.setTitulo(dto.titulo());
        }

        if (dto.descricaoEvento() != null) {
            evento.setDescricaoEvento(dto.descricaoEvento());
        }

        if (dto.previsaoInicio() != null) {
            evento.setPrevisaoInicio(dto.previsaoInicio());
        }

        if (dto.previsaoFim() != null) {
            evento.setPrevisaoFim(dto.previsaoFim());
        }

        if (dto.status() != null) {
            evento.setStatus(dto.status());
        }

        if (dto.endereco() != null) {
            evento.setEndereco(dto.endereco());
        }
    }
}
