package com.fiap.reservas.api.service;

import com.fiap.reservas.api.domain.Sala;
import com.fiap.reservas.api.dto.AtualizarSalaDto;
import com.fiap.reservas.api.dto.CriarSalaDto;
import com.fiap.reservas.api.dto.SalaDto;
import com.fiap.reservas.api.repository.SalaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalaService {
    private final SalaRepository repo;
    public SalaService(SalaRepository repo) { this.repo = repo; }

    // cria uma sala
    public SalaDto criar(CriarSalaDto dto) {
        Sala sala = new Sala();
        sala.setNome(dto.getNome());
        return toDto(repo.save(sala));
    }

    // lista todas as salas
    public List<SalaDto> listarTodos() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    // busca por id ou lança 404
    public SalaDto buscarPorId(Long id) {
        Sala sala = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada"));
        return toDto(sala);
    }

    // atualiza uma sala existente
    public SalaDto atualizar(Long id, AtualizarSalaDto dto) {
        Sala existente = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada"));
        existente.setNome(dto.getNome());
        return toDto(repo.save(existente));
    }

    // remove uma sala por id
    public void remover(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada");
        }
        repo.deleteById(id);
    }

    private SalaDto toDto(Sala sala) {
        SalaDto dto = new SalaDto();
        dto.setId(sala.getId());
        dto.setNome(sala.getNome());
        return dto;
    }
}