package com.fiap.reservas.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AtualizarSalaDto {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

}
