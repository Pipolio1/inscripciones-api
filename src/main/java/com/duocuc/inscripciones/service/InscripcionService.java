package com.duocuc.inscripciones.service;

import com.duocuc.inscripciones.dto.InscripcionRequest;
import com.duocuc.inscripciones.dto.InscripcionResponse;
import com.duocuc.inscripciones.dto.ResumenInscripcionMessage;
import com.duocuc.inscripciones.model.Inscripcion;
import com.duocuc.inscripciones.repository.InscripcionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final InscripcionProducer inscripcionProducer;
    private final S3Service s3Service;

    @Transactional
    public InscripcionResponse crearInscripcion(InscripcionRequest request) {
        Inscripcion inscripcion = Inscripcion.builder()
                .nombreEstudiante(request.getNombreEstudiante())
                .email(request.getEmail())
                .nombreCurso(request.getNombreCurso())
                .monto(request.getMonto())
                .build();

        Inscripcion guardada = inscripcionRepository.save(inscripcion);

        ResumenInscripcionMessage mensaje = ResumenInscripcionMessage.builder()
                .inscripcionId(guardada.getId())
                .nombreEstudiante(guardada.getNombreEstudiante())
                .nombreCurso(guardada.getNombreCurso())
                .monto(guardada.getMonto())
                .estado("PENDIENTE")
                .build();

        inscripcionProducer.enviarResumenInscripcion(mensaje);

        return mapToResponse(guardada);
    }

    @Transactional(readOnly = true)
    public List<InscripcionResponse> listarInscripciones() {
        return inscripcionRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InscripcionResponse obtenerInscripcion(Long id) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripcion no encontrada con id: " + id));
        return mapToResponse(inscripcion);
    }

    @Transactional(readOnly = true)
    public List<InscripcionResponse> listarPorEstado(String estado) {
        return inscripcionRepository.findByEstado(estado).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public InscripcionResponse subirComprobante(Long id, MultipartFile archivo) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripcion no encontrada con id: " + id));

        String key = s3Service.subirArchivo(archivo, "comprobantes-inscripciones");
        inscripcion.setNombreArchivoS3(key);
        inscripcion.setUrlArchivoS3(s3Service.generarUrlPublica(key));

        return mapToResponse(inscripcionRepository.save(inscripcion));
    }

    @Transactional(readOnly = true)
    public byte[] descargarComprobante(Long id) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripcion no encontrada con id: " + id));

        if (inscripcion.getNombreArchivoS3() == null) {
            throw new RuntimeException("La inscripcion no tiene comprobante adjunto");
        }

        return s3Service.descargarArchivo(inscripcion.getNombreArchivoS3());
    }

    @Transactional
    public void eliminarComprobante(Long id) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripcion no encontrada con id: " + id));

        if (inscripcion.getNombreArchivoS3() != null) {
            s3Service.eliminarArchivo(inscripcion.getNombreArchivoS3());
            inscripcion.setNombreArchivoS3(null);
            inscripcion.setUrlArchivoS3(null);
            inscripcionRepository.save(inscripcion);
        }
    }

    private InscripcionResponse mapToResponse(Inscripcion inscripcion) {
        return InscripcionResponse.builder()
                .id(inscripcion.getId())
                .nombreEstudiante(inscripcion.getNombreEstudiante())
                .email(inscripcion.getEmail())
                .nombreCurso(inscripcion.getNombreCurso())
                .fechaInscripcion(inscripcion.getFechaInscripcion())
                .monto(inscripcion.getMonto())
                .estado(inscripcion.getEstado())
                .urlArchivoS3(inscripcion.getUrlArchivoS3())
                .nombreArchivoS3(inscripcion.getNombreArchivoS3())
                .build();
    }
}
