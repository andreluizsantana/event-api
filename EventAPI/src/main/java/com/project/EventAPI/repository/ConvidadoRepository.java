package com.project.EventAPI.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.EventAPI.entity.Convidado;

public interface ConvidadoRepository extends JpaRepository<Convidado, Long> {

  Page<Convidado> findByEventoId(Long eventoId, Pageable pageable);

  Optional<Convidado> findByIdAndEventoId(Long id, Long eventoId);
}
