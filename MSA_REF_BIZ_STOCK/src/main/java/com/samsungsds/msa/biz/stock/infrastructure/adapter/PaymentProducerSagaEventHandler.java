package com.samsungsds.msa.biz.stock.infrastructure.adapter;

import com.samsungsds.msa.saga.event.SagaEvent;
import com.samsungsds.msa.saga.handler.ProducerSagaEventHandler;
import com.samsungsds.msa.saga.kafka.KafkaEventProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class PaymentProducerSagaEventHandler implements ProducerSagaEventHandler<SagaEvent<?>> {

    @Value("${kafka.producer.payment-success-topic}")
    private String paymentSuccessTopic;

    @Autowired
    private KafkaEventProcessor kafkaEventProcessor;


    @Override
    public void publishEvent(SagaEvent<?> sagaEvent) {
        sagaEvent.setEventType(paymentSuccessTopic);
        kafkaEventProcessor.publish(paymentSuccessTopic, sagaEvent);
    }
}
