// ...existing code...
package com.fiap.reservas.api.controller;

import com.fiap.reservas.api.dto.AtualizarSalaDto;
import com.fiap.reservas.api.dto.CriarSalaDto;
import com.fiap.reservas.api.dto.SalaDto;
import com.fiap.reservas.api.service.SalaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/api/sala")
public class SalaController {
    private final SalaService service;
    public SalaController(SalaService service) { this.service = service; }
    @PostMapping
    public SalaDto criar(@RequestBody @Valid CriarSalaDto dto) {
        return service.criar(dto);
    }

    @GetMapping("/all")
    public List<SalaDto> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public SalaDto buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public SalaDto atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarSalaDto dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}