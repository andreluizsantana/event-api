package com.project.EventAPI.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.project.EventAPI.dto.response.EventResponseDTO;
import com.project.EventAPI.enums.Status;
import com.project.EventAPI.exception.EventNotFoundException;
import com.project.EventAPI.service.EventService;

@WebMvcTest(EventController.class)
class EventControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private EventService eventService;

  private EventResponseDTO response() {
    return new EventResponseDTO(1L, "Festa", "Descrição", LocalDateTime.now(),
        LocalDateTime.now().plusDays(1), Status.PENDENTE, null);
  }

  @Test
  void deveListarEventos() throws Exception {
    when(eventService.listarEventos(any()))
        .thenReturn(new PageImpl<>(List.of(response()), PageRequest.of(0, 10), 1));

    mockMvc.perform(get("/api/events"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].titulo").value("Festa"));
  }

  @Test
  void deveBuscarEventoPorId() throws Exception {
    when(eventService.buscarID(1L)).thenReturn(response());

    mockMvc.perform(get("/api/events/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.titulo").value("Festa"));
  }

  @Test
  void deveRetornar404QuandoEventoNaoEncontrado() throws Exception {
    when(eventService.buscarID(99L)).thenThrow(new EventNotFoundException(99L));

    mockMvc.perform(get("/api/events/99"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404));
  }

  @Test
  void deveCriarEvento() throws Exception {
    when(eventService.salvarEvento(any())).thenReturn(response());

    String json = """
        {
          "titulo": "Festa",
          "descricaoEvento": "Descrição",
          "previsaoInicio": "2026-01-01T10:00:00",
          "previsaoFim": "2026-01-02T10:00:00",
          "status": "PENDENTE"
        }
        """;

    mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.titulo").value("Festa"));
  }

  @Test
  void deveRetornar400QuandoDadosInvalidos() throws Exception {
    String json = """
        {
          "titulo": "",
          "descricaoEvento": ""
        }
        """;

    mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.erros.titulo").exists())
        .andExpect(jsonPath("$.erros.descricaoEvento").exists())
        .andExpect(jsonPath("$.erros.previsaoInicio").exists())
        .andExpect(jsonPath("$.erros.previsaoFim").exists());
  }

  @Test
  void deveAtualizarEvento() throws Exception {
    when(eventService.atualizarEvento(eq(1L), any())).thenReturn(response());

    String json = """
        { "titulo": "Novo título" }
        """;

    mockMvc.perform(put("/api/events/1").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk());
  }

  @Test
  void deveDeletarEvento() throws Exception {
    doNothing().when(eventService).deletarEvento(1L);

    mockMvc.perform(delete("/api/events/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  void deveRetornar404AoDeletarEventoInexistente() throws Exception {
    doThrow(new EventNotFoundException(99L)).when(eventService).deletarEvento(99L);

    mockMvc.perform(delete("/api/events/99"))
        .andExpect(status().isNotFound());
  }
}
