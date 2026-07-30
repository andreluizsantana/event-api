package com.project.EventAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.EventAPI.entity.Fornecedor;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
}
