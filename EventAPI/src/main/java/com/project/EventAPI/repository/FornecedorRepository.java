package com.project.EventAPI.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.EventAPI.entity.Fornecedor;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

  Page<Fornecedor> findByEventoId(Long eventoId, Pageable pageable);

  Optional<Fornecedor> findByIdAndEventoId(Long id, Long eventoId);

  boolean existsByEventoId(Long eventoId);
}
