package com.project.EventAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.EventAPI.entity.Convidado;

public interface ConvidadoRepository extends JpaRepository<Convidado, Long> {
}
