package com.fiap.reservas.api.service;

import com.fiap.reservas.api.domain.Sala;
import com.fiap.reservas.api.dto.CriarReservaDto;
import com.fiap.reservas.api.repository.ReservaRepository;
import com.fiap.reservas.api.repository.SalaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservaServiceTest {

    @Test
    void criar_deveLancarBadRequest_quandoIntervaloInvalido() {
        ReservaRepository reservaRepo = mock(ReservaRepository.class);
        SalaRepository salaRepo = mock(SalaRepository.class);
        ReservaService service = new ReservaService(reservaRepo, salaRepo);

        CriarReservaDto dto = new CriarReservaDto();
        dto.setSalaId(1L);
        dto.setInicioReserva(LocalDateTime.of(2026, 1, 10, 10, 0));
        dto.setFinalReserva(LocalDateTime.of(2026, 1, 10, 10, 0)); // fim == inicio
        dto.setNomeResponsavel("Maria");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.criar(dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());

        verifyNoInteractions(salaRepo);
        verifyNoInteractions(reservaRepo);
    }

    @Test
    void criar_deveLancarConflict_quandoExisteConflitoDeHorarioNaMesmaSala() {
        ReservaRepository reservaRepo = mock(ReservaRepository.class);
        SalaRepository salaRepo = mock(SalaRepository.class);
        ReservaService service = new ReservaService(reservaRepo, salaRepo);

        Sala sala = new Sala();
        sala.setId(10L);
        sala.setNome("Sala 101");

        when(salaRepo.findById(10L)).thenReturn(Optional.of(sala));
        when(reservaRepo.existsBySalaIdAndCanceladaFalseAndInicioReservaLessThanAndFinalReservaGreaterThan(
                eq(10L),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(true);

        CriarReservaDto dto = new CriarReservaDto();
        dto.setSalaId(10L);
        dto.setInicioReserva(LocalDateTime.of(2026, 1, 10, 10, 0));
        dto.setFinalReserva(LocalDateTime.of(2026, 1, 10, 11, 0));
        dto.setNomeResponsavel("João");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.criar(dto));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());

        verify(salaRepo).findById(10L);
        verify(reservaRepo).existsBySalaIdAndCanceladaFalseAndInicioReservaLessThanAndFinalReservaGreaterThan(
                eq(10L),
                eq(dto.getFinalReserva()),
                eq(dto.getInicioReserva())
        );
        verify(reservaRepo, never()).save(any());
    }
}
