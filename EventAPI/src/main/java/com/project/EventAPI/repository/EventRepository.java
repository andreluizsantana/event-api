package com.project.EventAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.EventAPI.entity.Evento;

public interface EventRepository extends JpaRepository<Evento, Long> {
}
