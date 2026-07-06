package com.duocuc.inscripciones.service;

import com.duocuc.inscripciones.dto.ResumenInscripcionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResumenInscripcionConsumer {

    private final ResumenInscripcionService resumenService;

    @RabbitListener(queues = "${cola.inscripciones.nombre}")
    public void recibirMensaje(ResumenInscripcionMessage mensaje) {
        log.info("Mensaje recibido desde la cola: {}", mensaje);
        resumenService.guardarResumen(mensaje);
        log.info("Mensaje procesado y resumen guardado exitosamente");
    }
}
