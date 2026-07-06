package com.duocuc.inscripciones.service;

import com.duocuc.inscripciones.dto.ResumenInscripcionMessage;
import com.duocuc.inscripciones.dto.ResumenInscripcionResponse;
import com.duocuc.inscripciones.model.ResumenInscripcion;
import com.duocuc.inscripciones.repository.ResumenInscripcionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumenInscripcionService {

    private final ResumenInscripcionRepository resumenRepository;

    @Transactional
    public ResumenInscripcionResponse guardarResumen(ResumenInscripcionMessage mensaje) {
        log.info("Guardando resumen de inscripcion: {}", mensaje);

        ResumenInscripcion resumen = ResumenInscripcion.builder()
                .inscripcionId(mensaje.getInscripcionId())
                .nombreEstudiante(mensaje.getNombreEstudiante())
                .nombreCurso(mensaje.getNombreCurso())
                .monto(mensaje.getMonto())
                .estado(mensaje.getEstado())
                .build();

        ResumenInscripcion guardado = resumenRepository.save(resumen);
        log.info("Resumen guardado con id: {}", guardado.getId());

        return mapToResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<ResumenInscripcionResponse> listarResumenes() {
        return resumenRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResumenInscripcionResponse> listarPorInscripcionId(Long inscripcionId) {
        return resumenRepository.findByInscripcionId(inscripcionId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ResumenInscripcionResponse mapToResponse(ResumenInscripcion resumen) {
        return ResumenInscripcionResponse.builder()
                .id(resumen.getId())
                .inscripcionId(resumen.getInscripcionId())
                .nombreEstudiante(resumen.getNombreEstudiante())
                .nombreCurso(resumen.getNombreCurso())
                .monto(resumen.getMonto())
                .fechaProcesamiento(resumen.getFechaProcesamiento())
                .estado(resumen.getEstado())
                .build();
    }
}
