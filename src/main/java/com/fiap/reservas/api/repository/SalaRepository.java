package com.fiap.reservas.api.repository;

import com.fiap.reservas.api.domain.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    List<Sala> findByNomeContainingIgnoreCase(String nome);
}