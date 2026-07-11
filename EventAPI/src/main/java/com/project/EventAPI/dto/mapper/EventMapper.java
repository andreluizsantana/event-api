package com.project.EventAPI.dto.mapper;

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
}
