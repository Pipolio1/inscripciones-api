package com.duocuc.inscripciones.service;

import com.duocuc.inscripciones.dto.ResumenInscripcionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InscripcionProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${cola.inscripciones.exchange}")
    private String exchange;

    @Value("${cola.inscripciones.routing-key}")
    private String routingKey;

    public void enviarResumenInscripcion(ResumenInscripcionMessage mensaje) {
        log.info("Enviando mensaje a la cola: {}", mensaje);
        rabbitTemplate.convertAndSend(exchange, routingKey, mensaje);
        log.info("Mensaje enviado correctamente");
    }
}
