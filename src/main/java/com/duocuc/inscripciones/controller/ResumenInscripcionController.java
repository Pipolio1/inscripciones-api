package com.duocuc.inscripciones.controller;

import com.duocuc.inscripciones.dto.ResumenInscripcionResponse;
import com.duocuc.inscripciones.service.ResumenInscripcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumen-inscripciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ResumenInscripcionController {

    private final ResumenInscripcionService resumenService;

    @GetMapping
    public ResponseEntity<List<ResumenInscripcionResponse>> listarResumenes() {
        return ResponseEntity.ok(resumenService.listarResumenes());
    }

    @GetMapping("/inscripcion/{inscripcionId}")
    public ResponseEntity<List<ResumenInscripcionResponse>> listarPorInscripcionId(@PathVariable Long inscripcionId) {
        return ResponseEntity.ok(resumenService.listarPorInscripcionId(inscripcionId));
    }
}
