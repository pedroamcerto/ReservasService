package com.fiap.reservas.api.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime inicioReserva;

    @Column(nullable = false)
    private LocalDateTime finalReserva;

    @Column(nullable = false)
    private String nomeResponsavel;

    @Column(nullable = false)
    private boolean cancelada = false;

    // Uma sala pode ter várias reservas; uma reserva pertence a uma sala
    @ManyToOne(optional = false)
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;
}