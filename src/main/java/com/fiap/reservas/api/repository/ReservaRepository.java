package com.fiap.reservas.api.repository;

import com.fiap.reservas.api.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsBySalaIdAndCanceladaFalseAndInicioReservaLessThanAndFinalReservaGreaterThan(
            Long salaId,
            LocalDateTime fim,
            LocalDateTime inicio
    );
}