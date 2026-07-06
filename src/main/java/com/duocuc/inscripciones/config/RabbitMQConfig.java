package com.duocuc.inscripciones.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${cola.inscripciones.nombre}")
    private String queueName;

    @Value("${cola.inscripciones.exchange}")
    private String exchangeName;

    @Value("${cola.inscripciones.routing-key}")
    private String routingKey;

    @Bean
    public Queue inscripcionesQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public DirectExchange inscripcionesExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding binding(Queue inscripcionesQueue, DirectExchange inscripcionesExchange) {
        return BindingBuilder
                .bind(inscripcionesQueue)
                .to(inscripcionesExchange)
                .with(routingKey);
    }

    @Bean
    public SimpleMessageConverter messageConverter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        converter.setAllowedListPatterns(java.util.List.of("com.duocuc.inscripciones.dto.*", "java.*", "java.math.*"));
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }
}
