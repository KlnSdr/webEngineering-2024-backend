package com.sks.surveys.api;

import com.sks.base.api.BaseQueueConfig;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SurveyQueueConfig implements BaseQueueConfig {
    public static final String REQUEST_QUEUE_NAME = "surveysQueue.request";
    public static final String RESPONSE_QUEUE_NAME = "surveysQueue.response";
    public static final String EXCHANGE_NAME = "surveysExchange";
    public static final String ROUTING_KEY_REQUEST = "surveysService.request.routingKey";

    @Bean(name = "surveysRequestQueue")
    public Queue requestQueue() {
        return new Queue(getRequestQueueName(), false);
    }

    @Bean(name = "surveysResponseQueue")
    public Queue responseQueue() {
        return new Queue(getResponseQueueName(), false);
    }

    @Bean(name = "surveysExchange")
    public DirectExchange surveysExchange() {
        return new DirectExchange(getExchangeName());
    }

    @Bean(name = "surveysRequestBinding")
    public Binding requestBinding(@Qualifier("surveysRequestQueue") Queue requestQueue, @Qualifier("surveysExchange") DirectExchange exchange) {
        return BindingBuilder.bind(requestQueue).to(exchange).with(getRequestRoutingKey());
    }

    @Bean(name = "surveysMessageConverter")
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name = "surveysRabbitTemplate")
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
