package com.duocuc.inscripciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "resumen_inscripciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumenInscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resumen_seq")
    @SequenceGenerator(name = "resumen_seq", sequenceName = "resumen_seq", allocationSize = 1)
    private Long id;

    @Column(name = "inscripcion_id", nullable = false)
    private Long inscripcionId;

    @Column(name = "nombre_estudiante", nullable = false)
    private String nombreEstudiante;

    @Column(name = "nombre_curso", nullable = false)
    private String nombreCurso;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha_procesamiento", nullable = false)
    private LocalDateTime fechaProcesamiento;

    @Column(name = "estado", nullable = false)
    private String estado;

    @PrePersist
    public void prePersist() {
        if (this.fechaProcesamiento == null) {
            this.fechaProcesamiento = LocalDateTime.now();
        }
        if (this.estado == null) {
            this.estado = "PROCESADO";
        }
    }
}
