package com.sks.fridge.api;

import com.sks.base.api.BaseQueueConfig;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FridgeQueueConfig implements BaseQueueConfig {
    public static final String REQUEST_QUEUE_NAME = "fridgeQueue.request";
    public static final String RESPONSE_QUEUE_NAME = "fridgeQueue.response";
    public static final String EXCHANGE_NAME = "fridgeExchange";
    public static final String ROUTING_KEY_REQUEST = "fridgeService.request.routingKey";

    @Bean(name = "fridgeRequestQueue")
    public Queue requestQueue() {
        return new Queue(getRequestQueueName(), false);
    }

    @Bean(name = "fridgeResponseQueue")
    public Queue responseQueue() {
        return new Queue(getResponseQueueName(), false);
    }

    @Bean(name = "fridgeExchange")
    public DirectExchange fridgeExchange() {
        return new DirectExchange(getExchangeName());
    }

    @Bean(name = "fridgeRequestBinding")
    public Binding requestBinding(@Qualifier("fridgeRequestQueue") Queue requestQueue, @Qualifier("fridgeExchange") DirectExchange exchange) {
        return BindingBuilder.bind(requestQueue).to(exchange).with(getRequestRoutingKey());
    }

    @Bean(name = "fridgeMessageConverter")
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name = "fridgeRabbitTemplate")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        template.setReplyTimeout(5000); // Set reply timeout
        return template;
    }

    @Override
    public String getRequestQueueName() {
        return REQUEST_QUEUE_NAME;
    }

    @Override
    public String getResponseQueueName() {
        return RESPONSE_QUEUE_NAME;
    }

    @Override
    public String getExchangeName() {
        return EXCHANGE_NAME;
    }

    @Override
    public String getRequestRoutingKey() {
        return ROUTING_KEY_REQUEST;
    }
}
