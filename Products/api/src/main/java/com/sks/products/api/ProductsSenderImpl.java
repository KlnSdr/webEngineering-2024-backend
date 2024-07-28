package com.sks.products.api;

import com.sks.base.api.BaseSenderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ProductsSenderImpl extends BaseSenderImpl<ProductsRequestMessage, ProductsResponseMessage, ProductsQueueConfig> implements ProductsSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductsSenderImpl.class);

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
        LOGGER.error(errorMessage);
        return new ProductsResponseMessage();
    }
}
