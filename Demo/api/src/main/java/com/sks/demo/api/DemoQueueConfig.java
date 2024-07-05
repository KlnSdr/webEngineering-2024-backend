package com.sks.demo.api;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoQueueConfig {
    public static final String REQUEST_QUEUE_NAME = "demoQueue.request";
    public static final String RESPONSE_QUEUE_NAME = "demoQueue.response";
    public static final String EXCHANGE_NAME = "demoExchange";
    public static final String ROUTING_KEY_REQUEST = "demoService.request.routingKey";

    @Bean(name = "demoRequestQueue")
    public Queue requestQueue() {
        return new Queue(REQUEST_QUEUE_NAME, false);
    }

    @Bean(name = "demoResponseQueue")
    public Queue responseQueue() {
        return new Queue(RESPONSE_QUEUE_NAME, false);
    }

    @Bean
    public DirectExchange demoExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding requestBinding(@Qualifier("demoRequestQueue") Queue requestQueue, DirectExchange exchange) {
        return BindingBuilder.bind(requestQueue).to(exchange).with(ROUTING_KEY_REQUEST);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        template.setReplyTimeout(5000); // Set reply timeout
        return template;
    }
}
