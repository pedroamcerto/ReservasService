package com.fiap.reservas.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CriarReservaDto {

    @NotNull(message = "Sala é obrigatória")
    private Long salaId;

    @NotNull(message = "Início da reserva é obrigatório")
    private LocalDateTime inicioReserva;

    @NotNull(message = "Final da reserva é obrigatório")
    private LocalDateTime finalReserva;

    @NotBlank(message = "Nome do responsável é obrigatório")
    private String nomeResponsavel;
}