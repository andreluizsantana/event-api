package com.project.EventAPI.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.project.EventAPI.dto.response.ConvidadoResponseDTO;
import com.project.EventAPI.exception.ResourceNotFoundException;
import com.project.EventAPI.service.ConvidadoService;

@WebMvcTest(ConvidadoController.class)
class ConvidadoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ConvidadoService convidadoService;

  private ConvidadoResponseDTO response() {
    return new ConvidadoResponseDTO(1L, "Ana", "ana@teste.com", "11999999999", false, 1L);
  }

  @Test
  void deveListarConvidados() throws Exception {
    when(convidadoService.listar(eq(1L), any()))
        .thenReturn(new PageImpl<>(List.of(response()), PageRequest.of(0, 10), 1));

    mockMvc.perform(get("/api/events/1/convidados"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].nome").value("Ana"));
  }

  @Test
  void deveBuscarConvidado() throws Exception {
    when(convidadoService.buscar(1L, 1L)).thenReturn(response());

    mockMvc.perform(get("/api/events/1/convidados/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("ana@teste.com"));
  }

  @Test
  void deveRetornar404QuandoConvidadoNaoEncontrado() throws Exception {
    when(convidadoService.buscar(1L, 99L)).thenThrow(new ResourceNotFoundException("não encontrado"));

    mockMvc.perform(get("/api/events/1/convidados/99"))
        .andExpect(status().isNotFound());
  }

  @Test
  void deveCriarConvidado() throws Exception {
    when(convidadoService.salvar(eq(1L), any())).thenReturn(response());

    mockMvc.perform(post("/api/events/1/convidados").contentType(MediaType.APPLICATION_JSON)
        .content("""
            { "nome": "Ana", "email": "ana@teste.com" }
            """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.nome").value("Ana"));
  }

  @Test
  void deveRetornar400QuandoNomeVazio() throws Exception {
    mockMvc.perform(post("/api/events/1/convidados").contentType(MediaType.APPLICATION_JSON)
        .content("""
            { "nome": "" }
            """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.erros.nome").exists());
  }

  @Test
  void deveDeletarConvidado() throws Exception {
    mockMvc.perform(delete("/api/events/1/convidados/1"))
        .andExpect(status().isNoContent());
  }
}
