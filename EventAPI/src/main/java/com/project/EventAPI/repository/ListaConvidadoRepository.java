package com.project.EventAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.EventAPI.entity.ListaConvidado;
import com.project.EventAPI.entity.ListaConvidadoId;

public interface ListaConvidadoRepository extends JpaRepository<ListaConvidado, ListaConvidadoId> {
}
