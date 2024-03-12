package com.samsungsds.msa.biz.stock.infrastructure.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsungsds.msa.saga.event.SagaEvent;
import com.samsungsds.msa.saga.handler.ProducerSagaEventHandler;
import com.samsungsds.msa.saga.kafka.KafkaEventProcessor;
import com.samsungsds.msa.saga.payload.PaymentStatusPayload;
import com.samsungsds.msa.saga.payload.StockStatusPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;

@Component
public class StockProducerSagaEventHandler implements ProducerSagaEventHandler<SagaEvent<?>> {
    @Value("${spring.application.name}")
    String applicationName;

    @Value("${kafka.producer.stock-success-topic}")
    String stockSuccessTopic;


    @Autowired
    private KafkaEventProcessor kafkaEventProcessor;

    @Autowired
    private PaymentProducerSagaEventHandler paymentProducerSagaEventHandler;

    @Override
    public void publishEvent(SagaEvent<?> sagaEvent) {
        SagaEvent<StockStatusPayload> stockStatusPayloadSagaEvent = new SagaEvent<>();
        stockStatusPayloadSagaEvent.setSagaId(sagaEvent.getSagaId());
        stockStatusPayloadSagaEvent.setSagaStatus(SagaEvent.SagaStatus.IN_PROGRESS);
        stockStatusPayloadSagaEvent.setEventOrigin(applicationName);

        sagaEvent.setEventOrder(sagaEvent.getEventOrder()+1);

        stockStatusPayloadSagaEvent.setEventOrder(sagaEvent.getEventOrder());
        stockStatusPayloadSagaEvent.setEventType(stockSuccessTopic);
        stockStatusPayloadSagaEvent.setTimestamp(DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())));

        ObjectMapper objectMapper = new ObjectMapper();
        PaymentStatusPayload paymentStatusPayload = objectMapper.convertValue(sagaEvent.getPayload(), PaymentStatusPayload.class);

        stockStatusPayloadSagaEvent.setServiceTransactionId(sagaEvent.getServiceTransactionId());

        StockStatusPayload stockStatusPayload = new StockStatusPayload();
        stockStatusPayload.setOrderId(paymentStatusPayload.getOrderId());
        stockStatusPayload.setPaymentId(paymentStatusPayload.getPaymentId());
        stockStatusPayload.setCount(paymentStatusPayload.getCount());
        stockStatusPayload.setOrderOrigin(paymentStatusPayload.getOrderOrigin());
        stockStatusPayloadSagaEvent.setPayload(stockStatusPayload);

        kafkaEventProcessor.publish(stockSuccessTopic, stockStatusPayloadSagaEvent);

        prepareAndPublishSuccessEvent(sagaEvent);
    }

    private void prepareAndPublishSuccessEvent(SagaEvent<?> sagaEvent) {
        sagaEvent.setSagaStatus(SagaEvent.SagaStatus.COMPLETED);
        sagaEvent.setEventOrigin(applicationName);
        sagaEvent.setServiceTransactionId("");
        sagaEvent.setEventOrder(sagaEvent.getEventOrder() + 1);
        sagaEvent.setTimestamp(DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())));
        paymentProducerSagaEventHandler.publishEvent(sagaEvent);
    }
}
