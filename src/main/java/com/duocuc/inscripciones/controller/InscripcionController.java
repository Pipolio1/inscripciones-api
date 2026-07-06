package com.duocuc.inscripciones.controller;

import com.duocuc.inscripciones.dto.InscripcionRequest;
import com.duocuc.inscripciones.dto.InscripcionResponse;
import com.duocuc.inscripciones.dto.MensajeResponse;
import com.duocuc.inscripciones.service.InscripcionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @PostMapping
    public ResponseEntity<InscripcionResponse> crearInscripcion(@Valid @RequestBody InscripcionRequest request) {
        InscripcionResponse response = inscripcionService.crearInscripcion(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InscripcionResponse>> listarInscripciones() {
        return ResponseEntity.ok(inscripcionService.listarInscripciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InscripcionResponse> obtenerInscripcion(@PathVariable Long id) {
        return ResponseEntity.ok(inscripcionService.obtenerInscripcion(id));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<InscripcionResponse>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(inscripcionService.listarPorEstado(estado));
    }

    @PostMapping("/{id}/subir-comprobante")
    public ResponseEntity<InscripcionResponse> subirComprobante(
            @PathVariable Long id,
            @RequestParam("archivo") MultipartFile archivo) {
        return ResponseEntity.ok(inscripcionService.subirComprobante(id, archivo));
    }

    @GetMapping("/{id}/descargar-comprobante")
    public ResponseEntity<byte[]> descargarComprobante(@PathVariable Long id) {
        byte[] archivo = inscripcionService.descargarComprobante(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comprobante.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(archivo);
    }

    @DeleteMapping("/{id}/eliminar-comprobante")
    public ResponseEntity<MensajeResponse> eliminarComprobante(@PathVariable Long id) {
        inscripcionService.eliminarComprobante(id);
        return ResponseEntity.ok(MensajeResponse.builder().mensaje("Comprobante eliminado correctamente").build());
    }
}
