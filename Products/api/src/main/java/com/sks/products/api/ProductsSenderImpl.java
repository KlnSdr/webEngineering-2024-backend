package com.sks.products.api;

import com.sks.base.api.BaseSenderImpl;
import com.sks.base.api.exceptions.MessageConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Implementation of the ProductsSender interface.
 * Extends the BaseSenderImpl class to provide specific functionality for product-related messages.
 */
@Component
public class ProductsSenderImpl extends BaseSenderImpl<ProductsRequestMessage, ProductsResponseMessage, ProductsQueueConfig> implements ProductsSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductsSenderImpl.class);

    /**
     * Constructs a ProductsSenderImpl with the specified AMQP template and queue configuration.
     *
     * @param amqpTemplate the AMQP template for sending messages
     * @param config the configuration for the products queue
     */
    public ProductsSenderImpl(@Qualifier("productsRabbitTemplate") AmqpTemplate amqpTemplate, ProductsQueueConfig config) {
        super(amqpTemplate, config);
    }

    /**
     * Converts the response object to a ProductsResponseMessage.
     *
     * @param response the response object to convert
     * @return the converted ProductsResponseMessage, or an error response if the conversion fails
     */
    @Override
    protected ProductsResponseMessage convertResponse(Object response) {
        if (response instanceof ProductsResponseMessage) {
            return (ProductsResponseMessage) response;
        }
        final ProductsResponseMessage errResponse = createErrorResponse("Invalid response");
        errResponse.setException(new MessageConversionException("Invalid response"));
        return errResponse;
    }

    /**
     * Creates an error response with the specified error message.
     *
     * @param errorMessage the error message to log and include in the response
     * @return a ProductsResponseMessage representing the error
     */
    @Override
    protected ProductsResponseMessage createErrorResponse(String errorMessage) {
        LOGGER.error(errorMessage);
        final ProductsResponseMessage response = new ProductsResponseMessage();
        response.setErrorMessage(errorMessage);
        response.setDidError(true);
        return response;
    }
}