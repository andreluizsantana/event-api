package com.project.EventAPI.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.EventAPI.entity.ItemEvento;

public interface ItemEventoRepository extends JpaRepository<ItemEvento, Long> {

  Page<ItemEvento> findByEventoId(Long eventoId, Pageable pageable);

  Optional<ItemEvento> findByIdAndEventoId(Long id, Long eventoId);

  boolean existsByEventoId(Long eventoId);
}
