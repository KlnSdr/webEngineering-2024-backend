package com.sks.recipes.api;

import com.sks.base.api.BaseQueueConfig;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecipesQueueConfig implements BaseQueueConfig {
    public static final String REQUEST_QUEUE_NAME = "recipesQueue.request";
    public static final String RESPONSE_QUEUE_NAME = "recipesQueue.response";
    public static final String EXCHANGE_NAME = "recipesExchange";
    public static final String ROUTING_KEY_REQUEST = "recipesService.request.routingKey";

    @Bean(name = "recipesRequestQueue")
    public Queue requestQueue() {
        return new Queue(getRequestQueueName(), false);
    }

    @Bean(name = "recipesResponseQueue")
    public Queue responseQueue() {
        return new Queue(getResponseQueueName(), false);
    }

    @Bean(name = "recipesExchange")
    public DirectExchange recipesExchange() {
        return new DirectExchange(getExchangeName());
    }

    @Bean(name = "recipesRequestBinding")
    public Binding requestBinding(@Qualifier("recipesRequestQueue") Queue requestQueue, @Qualifier("recipesExchange") DirectExchange exchange) {
        return BindingBuilder.bind(requestQueue).to(exchange).with(getRequestRoutingKey());
    }

    @Bean(name = "recipesMessageConverter")
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name = "recipesRabbitTemplate")
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
