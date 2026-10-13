package com.project.EventAPI.dto.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import com.project.EventAPI.dto.EnderecoDTO;
import com.project.EventAPI.dto.request.EventRequestDTO;
import com.project.EventAPI.dto.response.EventResponseDTO;
import com.project.EventAPI.dto.update.EventUpdateDTO;
import com.project.EventAPI.entity.Endereco;
import com.project.EventAPI.entity.Evento;
import com.project.EventAPI.enums.Status;

class EventMapperTest {

  private final EventMapper mapper = new EventMapperImpl();

  private Evento evento() {
    Evento evento = new Evento();
    evento.setTitulo("Festa");
    evento.setDescricaoEvento("Descrição");
    evento.setPrevisaoInicio(LocalDateTime.of(2026, 1, 1, 10, 0));
    evento.setPrevisaoFim(LocalDateTime.of(2026, 1, 2, 10, 0));
    evento.setStatus(Status.AGENDADO);
    evento.setEndereco(new Endereco("SP", "São Paulo", "Rua A", "10", "01000-000", "Centro"));
    return evento;
  }

  @Test
  void deveMapearEntidadeParaResponse() {
    EventResponseDTO response = mapper.entityToDto(evento());

    assertThat(response.titulo()).isEqualTo("Festa");
    assertThat(response.status()).isEqualTo(Status.AGENDADO);
    assertThat(response.endereco()).isNotNull();
    assertThat(response.endereco().cidade()).isEqualTo("São Paulo");
  }

  @Test
  void deveRetornarNullAoMapearEntidadeNula() {
    assertThat(mapper.entityToDto(null)).isNull();
  }

  @Test
  void deveMapearRequestParaEntidade() {
    EventRequestDTO dto = new EventRequestDTO("Festa", "Descrição", LocalDateTime.now(),
        LocalDateTime.now().plusDays(1), Status.PENDENTE, new EnderecoDTO("RJ", "Rio", null, null, null, null));

    Evento evento = mapper.dtoToEntity(dto);

    assertThat(evento.getTitulo()).isEqualTo("Festa");
    assertThat(evento.getStatus()).isEqualTo(Status.PENDENTE);
    assertThat(evento.getEndereco().getUf()).isEqualTo("RJ");
  }

  @Test
  void deveAtualizarApenasCamposNaoNulos() {
    Evento evento = evento();
    EventUpdateDTO dto = new EventUpdateDTO("Novo título", null, null, null, Status.CONCLUIDO, null);

    mapper.updateEntityFromDto(dto, evento);

    assertThat(evento.getTitulo()).isEqualTo("Novo título");
    assertThat(evento.getStatus()).isEqualTo(Status.CONCLUIDO);
    assertThat(evento.getDescricaoEvento()).isEqualTo("Descrição");
    assertThat(evento.getEndereco().getCidade()).isEqualTo("São Paulo");
  }

  @Test
  void deveAtualizarEnderecoParcialmenteSemApagarCampos() {
    Evento evento = evento();
    EventUpdateDTO dto = new EventUpdateDTO(null, null, null, null, null,
        new EnderecoDTO(null, "Campinas", null, null, null, null));

    mapper.updateEntityFromDto(dto, evento);

    assertThat(evento.getEndereco().getCidade()).isEqualTo("Campinas");
    assertThat(evento.getEndereco().getUf()).isEqualTo("SP");
  }
}
