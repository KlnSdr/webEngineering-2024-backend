package com.sks.demo.api;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public interface DemoListener {
    @RabbitListener(queues = DemoQueueConfig.REQUEST_QUEUE_NAME)
    void listen(DemoRequestMessage in);
}
