package com.duocuc.inscripciones.repository;

import com.duocuc.inscripciones.model.ResumenInscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumenInscripcionRepository extends JpaRepository<ResumenInscripcion, Long> {

    List<ResumenInscripcion> findByInscripcionId(Long inscripcionId);
}
