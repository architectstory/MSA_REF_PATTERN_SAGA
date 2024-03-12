package com.samsungsds.msa.biz.payment.infrastructure.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsungsds.msa.saga.event.SagaEvent;
import com.samsungsds.msa.saga.handler.ProducerSagaEventHandler;
import com.samsungsds.msa.saga.kafka.KafkaEventProcessor;
import com.samsungsds.msa.saga.payload.OrderStatusPayload;
import com.samsungsds.msa.saga.payload.PaymentStatusPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;

@Component
public class PaymentProducerSagaEventHandler implements ProducerSagaEventHandler<SagaEvent<?>> {

    @Value("${spring.application.name}")
    String applicationName;

    @Value("${kafka.producer.payment-success-topic}")
    private String paymentSuccessTopic;

    @Autowired
    private KafkaEventProcessor kafkaEventProcessor;


    @Override
    public void publishEvent(SagaEvent<?> sagaEvent) {
        //payment saga event
        SagaEvent<PaymentStatusPayload> paymentStatusPayloadSagaEvent = new SagaEvent<>();
        paymentStatusPayloadSagaEvent.setSagaId(sagaEvent.getSagaId());
        paymentStatusPayloadSagaEvent.setSagaStatus(SagaEvent.SagaStatus.IN_PROGRESS);
        paymentStatusPayloadSagaEvent.setEventOrigin(applicationName);
        paymentStatusPayloadSagaEvent.setEventOrder(sagaEvent.getEventOrder() + 1);
        paymentStatusPayloadSagaEvent.setEventType(paymentSuccessTopic);
        paymentStatusPayloadSagaEvent.setTimestamp(DateFormat.getTimeInstance().format(new Date(System.currentTimeMillis())));

        //order payload
        ObjectMapper objectMapper = new ObjectMapper();
        OrderStatusPayload orderStatusPayload = objectMapper.convertValue(sagaEvent.getPayload(), OrderStatusPayload.class);

        paymentStatusPayloadSagaEvent.setServiceTransactionId(sagaEvent.getServiceTransactionId());

        //payment payload
        PaymentStatusPayload paymentStatusPayload = new PaymentStatusPayload();
        paymentStatusPayload.setOrderId(orderStatusPayload.getOrderId());
        paymentStatusPayload.setPaymentId(sagaEvent.getServiceTransactionId());
        paymentStatusPayload.setCount(orderStatusPayload.getCount());
        paymentStatusPayload.setOrderOrigin(orderStatusPayload.getOrderOrigin());

        //payment saga event + payment payload
        paymentStatusPayloadSagaEvent.setPayload(paymentStatusPayload);

        //publish
        kafkaEventProcessor.publish(paymentSuccessTopic, paymentStatusPayloadSagaEvent);
    }
}
