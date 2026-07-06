package com.duocuc.inscripciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscripciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inscripcion_seq")
    @SequenceGenerator(name = "inscripcion_seq", sequenceName = "inscripcion_seq", allocationSize = 1)
    private Long id;

    @Column(name = "nombre_estudiante", nullable = false)
    private String nombreEstudiante;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nombre_curso", nullable = false)
    private String nombreCurso;

    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDateTime fechaInscripcion;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "url_archivo_s3")
    private String urlArchivoS3;

    @Column(name = "nombre_archivo_s3")
    private String nombreArchivoS3;

    @PrePersist
    public void prePersist() {
        if (this.fechaInscripcion == null) {
            this.fechaInscripcion = LocalDateTime.now();
        }
        if (this.estado == null) {
            this.estado = "PENDIENTE";
        }
    }
}
