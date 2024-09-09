package com.sks.recipes.api;

import com.sks.base.api.BaseSenderImpl;
import com.sks.base.api.exceptions.MessageConversionException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Implementation of the RecipeSender interface for sending recipe-related requests and responses.
 */
@Component
public class RecipeSenderImpl extends BaseSenderImpl<RecipeRequestMessage, RecipeResponseMessage, RecipesQueueConfig> implements RecipeSender {

    /**
     * Constructs a new RecipeSenderImpl with the specified AMQP template and queue configuration.
     *
     * @param amqpTemplate the AMQP template to use for sending messages
     * @param config the queue configuration
     */
    public RecipeSenderImpl(@Qualifier("recipesRabbitTemplate") AmqpTemplate amqpTemplate, RecipesQueueConfig config) {
        super(amqpTemplate, config);
    }

    /**
     * Converts the given response object to a RecipeResponseMessage.
     *
     * @param response the response object to convert
     * @return the converted RecipeResponseMessage, or an error response if the conversion fails
     */
    @Override
    protected RecipeResponseMessage convertResponse(Object response) {
        if (response instanceof RecipeResponseMessage) {
            return (RecipeResponseMessage) response;
        }
        final RecipeResponseMessage errResponse = createErrorResponse("Invalid response");
        errResponse.setException(new MessageConversionException("Invalid response"));
        return errResponse;
    }

    /**
     * Creates an error response with the specified error message.
     *
     * @param errorMessage the error message to include in the response
     * @return the created error response
     */
    @Override
    protected RecipeResponseMessage createErrorResponse(String errorMessage) {
        RecipeResponseMessage response = new RecipeResponseMessage();
        response.setErrorMessage(errorMessage);
        response.setDidError(true);
        return response;
    }
}