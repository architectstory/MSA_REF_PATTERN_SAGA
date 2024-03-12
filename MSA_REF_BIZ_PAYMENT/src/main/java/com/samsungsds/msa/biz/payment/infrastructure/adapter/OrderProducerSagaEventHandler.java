package com.samsungsds.msa.biz.payment.infrastructure.adapter;

import com.samsungsds.msa.saga.event.SagaEvent;
import com.samsungsds.msa.saga.handler.ProducerSagaEventHandler;
import com.samsungsds.msa.saga.kafka.KafkaEventProcessor;
import com.samsungsds.msa.saga.payload.OrderStatusPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;

@Component
public class OrderProducerSagaEventHandler implements ProducerSagaEventHandler<SagaEvent<?>> {
    @Value("${spring.application.name}")
    String applicationName;

    @Value("${kafka.producer.order-success-topic}")
    String orderSuccessTopic;

    @Value("${kafka.producer.order-failed-topic}")
    String orderFailedTopic;

    @Autowired
    private KafkaEventProcessor kafkaEventProcessor;

    @Override
    public void publishEvent(SagaEvent<?> sagaEvent) {
        SagaEvent<OrderStatusPayload> orderStatusPayloadSagaEvent = new SagaEvent<>();
        orderStatusPayloadSagaEvent.setSagaId(sagaEvent.getSagaId());
        orderStatusPayloadSagaEvent.setEventOrigin(applicationName);
        orderStatusPayloadSagaEvent.setEventType(orderSuccessTopic);
        orderStatusPayloadSagaEvent.setSagaStatus(SagaEvent.SagaStatus.COMPLETED);
        orderStatusPayloadSagaEvent.setEventOrder(sagaEvent.getEventOrder() + 1);
        orderStatusPayloadSagaEvent.setServiceTransactionId("");
        orderStatusPayloadSagaEvent.setTimestamp(DateFormat.getDateInstance().format(new Date(System.currentTimeMillis())));
        kafkaEventProcessor.publish(orderSuccessTopic, orderStatusPayloadSagaEvent);
    }
}
