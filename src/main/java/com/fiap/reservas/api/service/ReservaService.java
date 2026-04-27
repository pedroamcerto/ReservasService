package com.fiap.reservas.api.service;

import com.fiap.reservas.api.domain.Reserva;
import com.fiap.reservas.api.domain.Sala;
import com.fiap.reservas.api.dto.CriarReservaDto;
import com.fiap.reservas.api.dto.ReservaDto;
import com.fiap.reservas.api.repository.ReservaRepository;
import com.fiap.reservas.api.repository.SalaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepo;
    private final SalaRepository salaRepo;

    public ReservaService(ReservaRepository reservaRepo, SalaRepository salaRepo) {
        this.reservaRepo = reservaRepo;
        this.salaRepo = salaRepo;
    }

    public ReservaDto criar(CriarReservaDto dto) {
        validarIntervalo(dto.getInicioReserva(), dto.getFinalReserva());

        Sala sala = salaRepo.findById(dto.getSalaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada"));

        boolean conflito = reservaRepo.existsBySalaIdAndCanceladaFalseAndInicioReservaLessThanAndFinalReservaGreaterThan(
                sala.getId(),
                dto.getFinalReserva(),
                dto.getInicioReserva()
        );

        if (conflito) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe reserva para esta sala nesse horário");
        }

        Reserva r = new Reserva();
        r.setSala(sala);
        r.setInicioReserva(dto.getInicioReserva());
        r.setFinalReserva(dto.getFinalReserva());
        r.setNomeResponsavel(dto.getNomeResponsavel());
        r.setCancelada(false);

        return toDto(reservaRepo.save(r));
    }

    public List<ReservaDto> listarTodos() {
        return reservaRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public void cancelar(Long id) {
        Reserva r = reservaRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada"));

        if (r.isCancelada()) return;

        r.setCancelada(true);
        reservaRepo.save(r);
    }

    private void validarIntervalo(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Intervalo inválido");
        }
        if (!fim.isAfter(inicio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Final deve ser após o início");
        }
    }

    private ReservaDto toDto(Reserva r) {
        ReservaDto dto = new ReservaDto();
        dto.setId(r.getId());
        dto.setInicioReserva(r.getInicioReserva());
        dto.setFinalReserva(r.getFinalReserva());
        dto.setNomeResponsavel(r.getNomeResponsavel());
        dto.setCancelada(r.isCancelada());

        if (r.getSala() != null) {
            dto.setSalaId(r.getSala().getId());
            dto.setSalaNome(r.getSala().getNome());
        }

        return dto;
    }
}