package com.sks.products.api;

import com.sks.base.api.BaseQueueConfig;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductsQueueConfig implements BaseQueueConfig {
    public static final String REQUEST_QUEUE_NAME = "productsQueue.request";
    public static final String RESPONSE_QUEUE_NAME = "productsQueue.response";
    public static final String EXCHANGE_NAME = "productsExchange";
    public static final String ROUTING_KEY_REQUEST = "productsService.request.routingKey";

    @Bean(name = "productsRequestQueue")
    public Queue requestQueue() {
        return new Queue(getRequestQueueName(), false);
    }

    @Bean(name = "productsResponseQueue")
    public Queue responseQueue() {
        return new Queue(getResponseQueueName(), false);
    }

    @Bean (name = "productsExchange")
    public DirectExchange productsExchange() {
        return new DirectExchange(getExchangeName());
    }

    @Bean(name = "productsRequestBinding")
    public Binding requestBinding(@Qualifier("productsRequestQueue") Queue requestQueue, @Qualifier("productsExchange") DirectExchange exchange) {
        return BindingBuilder.bind(requestQueue).to(exchange).with(getRequestRoutingKey());
    }

    @Bean(name = "productsMessageConverter")
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name = "productsRabbitTemplate")
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
