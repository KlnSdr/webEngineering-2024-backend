package com.sks.demo.api;

import com.sks.base.api.BaseSenderImpl;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class DemoSenderImpl extends BaseSenderImpl<DemoRequestMessage, DemoResponseMessage, DemoQueueConfig> implements DemoSender {

    public DemoSenderImpl(AmqpTemplate amqpTemplate, DemoQueueConfig config) {
        super(amqpTemplate, config);
    }

    @Override
    protected DemoResponseMessage convertResponse(Object response) {
        if (response instanceof DemoResponseMessage) {
            return (DemoResponseMessage) response;
        }
        return createErrorResponse("Invalid response");
    }

    @Override
    protected DemoResponseMessage createErrorResponse(String errorMessage) {
        DemoResponseMessage response = new DemoResponseMessage();
        response.setMessage(errorMessage);
        return response;
    }
}
