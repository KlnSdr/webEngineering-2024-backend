package com.sks.surveys.api;

import com.sks.base.api.BaseSenderImpl;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SurveySenderImpl extends BaseSenderImpl<SurveyRequestMessage, SurveyResponseMessage, SurveyQueueConfig> implements SurveySender {

    public SurveySenderImpl(@Qualifier("surveysRabbitTemplate") AmqpTemplate amqpTemplate, SurveyQueueConfig config) {
        super(amqpTemplate, config);
    }

    @Override
    protected SurveyResponseMessage convertResponse(Object response) {
        if (response instanceof SurveyResponseMessage) {
            return (SurveyResponseMessage) response;
        }
        return createErrorResponse("Invalid response");
    }

    @Override
    protected SurveyResponseMessage createErrorResponse(String errorMessage) {
        SurveyResponseMessage response = new SurveyResponseMessage();
        response.setMessage(errorMessage);
        return response;
    }
}
