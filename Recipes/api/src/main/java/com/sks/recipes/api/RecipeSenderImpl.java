package com.sks.recipes.api;

import com.sks.base.api.BaseSenderImpl;
import com.sks.base.api.exceptions.MessageConversionException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class RecipeSenderImpl extends BaseSenderImpl<RecipeRequestMessage, RecipeResponseMessage, RecipesQueueConfig> implements RecipeSender {

    public RecipeSenderImpl(@Qualifier("recipesRabbitTemplate") AmqpTemplate amqpTemplate, RecipesQueueConfig config) {
        super(amqpTemplate, config);
    }

    @Override
    protected RecipeResponseMessage convertResponse(Object response) {
        if (response instanceof RecipeResponseMessage) {
            return (RecipeResponseMessage) response;
        }
        final RecipeResponseMessage errResponse = createErrorResponse("Invalid response");
        errResponse.setException(new MessageConversionException("Invalid response"));
        return errResponse;
    }

    @Override
    protected RecipeResponseMessage createErrorResponse(String errorMessage) {
        RecipeResponseMessage response = new RecipeResponseMessage();
        response.setErrorMessage(errorMessage);
        response.setDidError(true);
        return response;
    }
}
