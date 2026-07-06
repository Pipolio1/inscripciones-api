package com.duocuc.inscripciones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumenInscripcionMessage implements Serializable {

    private Long inscripcionId;
    private String nombreEstudiante;
    private String nombreCurso;
    private BigDecimal monto;
    private String estado;
}
