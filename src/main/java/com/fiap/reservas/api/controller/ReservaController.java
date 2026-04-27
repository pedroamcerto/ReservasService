package com.fiap.reservas.api.controller;

import com.fiap.reservas.api.domain.Reserva;
import com.fiap.reservas.api.dto.CriarReservaDto;
import com.fiap.reservas.api.dto.ReservaDto;
import com.fiap.reservas.api.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reserva")
public class ReservaController {

    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @PostMapping
    public ReservaDto criar(@RequestBody @Valid CriarReservaDto dto) {
        return service.criar(dto);
    }

    @GetMapping
    public List<ReservaDto> listar() {
        return service.listarTodos();
    }

    @DeleteMapping("/{id}")
    public void cancelar(@PathVariable Long id) {
        service.cancelar(id);
    }
}