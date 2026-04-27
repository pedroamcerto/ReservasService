package com.fiap.reservas.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservaDto {
    private Long id;
    private Long salaId;
    private String salaNome;
    private LocalDateTime inicioReserva;
    private LocalDateTime finalReserva;
    private String nomeResponsavel;
    private boolean cancelada;
}
