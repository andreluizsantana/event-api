package com.project.EventAPI.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.EventAPI.dto.response.EventResponseDTO;
import com.project.EventAPI.service.EventService;

@RestController
@RequestMapping("/api/event")
public class EventController {

  private final EventService eventservice;

  public EventController(EventService eventservice) {
    super();
    this.eventservice = eventservice;
  }

  @GetMapping
  public Page<EventResponseDTO> listarEventos(Pageable pageable) {
    return eventservice.listarEventos(pageable);
  }

}
