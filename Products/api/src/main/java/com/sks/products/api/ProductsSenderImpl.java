package com.sks.products.api;

import com.sks.base.api.BaseSenderImpl;
import com.sks.base.api.exceptions.MessageConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
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
        final ProductsResponseMessage errResponse = createErrorResponse("Invalid response");
        errResponse.setException(new MessageConversionException("Invalid response"));
        return errResponse;
    }

    @Override
    protected ProductsResponseMessage createErrorResponse(String errorMessage) {
        LOGGER.error(errorMessage);
        final ProductsResponseMessage response = new ProductsResponseMessage();
        response.setErrorMessage(errorMessage);
        response.setDidError(true);
        return response;
    }
}
