package com.sks.products.api;

import com.sks.base.api.BaseSenderImpl;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ProductsSenderImpl extends BaseSenderImpl<ProductsRequestMessage, ProductsResponseMessage, ProductsQueueConfig> implements ProductsSender {

    public ProductsSenderImpl(@Qualifier("productsRabbitTemplate") AmqpTemplate amqpTemplate, ProductsQueueConfig config) {
        super(amqpTemplate, config);
    }

    @Override
    protected ProductsResponseMessage convertResponse(Object response) {
        if (response instanceof ProductsResponseMessage) {
            return (ProductsResponseMessage) response;
        }
        return createErrorResponse("Invalid response");
    }

    @Override
    protected ProductsResponseMessage createErrorResponse(String errorMessage) {
        ProductsResponseMessage response = new ProductsResponseMessage();
        response.setMessage(errorMessage);
        return response;
    }
}
