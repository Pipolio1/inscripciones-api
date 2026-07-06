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
public class InscripcionResponse {

    private Long id;
    private String nombreEstudiante;
    private String email;
    private String nombreCurso;
    private LocalDateTime fechaInscripcion;
    private BigDecimal monto;
    private String estado;
    private String urlArchivoS3;
    private String nombreArchivoS3;
}
