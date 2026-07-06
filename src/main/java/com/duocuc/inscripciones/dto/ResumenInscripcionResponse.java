package com.duocuc.inscripciones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumenInscripcionResponse {

    private Long id;
    private Long inscripcionId;
    private String nombreEstudiante;
    private String nombreCurso;
    private BigDecimal monto;
    private LocalDateTime fechaProcesamiento;
    private String estado;
}
