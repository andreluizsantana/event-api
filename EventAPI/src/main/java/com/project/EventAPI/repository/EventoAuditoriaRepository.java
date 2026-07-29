package com.project.EventAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.EventAPI.entity.EventoAuditoria;

public interface EventoAuditoriaRepository extends JpaRepository<EventoAuditoria, Long> {
}