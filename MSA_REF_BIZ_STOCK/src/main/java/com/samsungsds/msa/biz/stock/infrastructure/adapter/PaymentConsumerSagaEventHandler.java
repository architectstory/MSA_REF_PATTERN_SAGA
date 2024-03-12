package com.samsungsds.msa.biz.stock.infrastructure.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsungsds.msa.biz.stock.application.StockDTO;
import com.samsungsds.msa.biz.stock.application.StockService;
import com.samsungsds.msa.saga.event.SagaEvent;
import com.samsungsds.msa.saga.handler.ConsumerSagaEventHandler;
import com.samsungsds.msa.saga.kafka.KafkaEventProcessor;
import com.samsungsds.msa.saga.payload.PaymentStatusPayload;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;


@Component
public class PaymentConsumerSagaEventHandler implements ConsumerSagaEventHandler<SagaEvent<?>> {
    @Value("${spring.application.name}")
    String applicationName;

    @Value("${kafka.producer.payment-success-topic}")
    private String paymentSuccessTopic;

    @Value("${kafka.producer.payment-failed-topic}")
    private String paymentFailedTopic;


    @Autowired
    private KafkaEventProcessor kafkaEventProcessor;

    @Autowired
    private StockProducerSagaEventHandler stockProducerSagaEventHandler;

    private final StockService stockService;

    public PaymentConsumerSagaEventHandler(StockService stockService)
    {
        this.stockService = stockService;
    }

    @Transactional
    @KafkaListener(topics = {"${kafka.consumer.payment-success-topic}"}, groupId = "${kafka.consumer.group-id")
    @Override
    public void transactionEvent(SagaEvent<?> transactionEvent) {
        ObjectMapper objectMapper = new ObjectMapper();
        PaymentStatusPayload paymentStatusPayload = objectMapper.convertValue(transactionEvent.getPayload(), PaymentStatusPayload.class);
        int quantity = paymentStatusPayload.getCount();
        //compensation case
        if(quantity > 100){
            paymentStatusPayload.setPaymentDescription("Currently, This item is not available in stock");

            SagaEvent<PaymentStatusPayload> PaymentStatusPayloadSagaEvent = prepareSagaEvent(transactionEvent);
            compensateEvent(PaymentStatusPayloadSagaEvent);

        }
        else {
            StockDTO stockDTO = new StockDTO();
            stockDTO.setOrigin(paymentStatusPayload.getOrderOrigin());
            stockDTO.setCount(quantity);

            stockService.createStock(stockDTO);
            transactionEvent.setServiceTransactionId(String.valueOf(transactionEvent.getServiceTransactionId()));

            stockProducerSagaEventHandler.publishEvent(transactionEvent);

            prepareAndPublishSuccessEvent(transactionEvent);
        }
    }

    @Override
    public void compensateEvent(SagaEvent<?> sagaEvent) {

        sagaEvent.setEventType(paymentFailedTopic);
        kafkaEventProcessor.publish(paymentFailedTopic, sagaEvent);

    }

    private void prepareAndPublishSuccessEvent(SagaEvent<?> sagaEvent){
        sagaEvent.setSagaStatus(SagaEvent.SagaStatus.COMPLETED);
        sagaEvent.setEventOrigin(applicationName);
        sagaEvent.setEventType(paymentSuccessTopic);
        sagaEvent.setServiceTransactionId("");
        sagaEvent.setEventOrder(sagaEvent.getEventOrder()+1);
        sagaEvent.setTimestamp(DateFormat.getTimeInstance().format(new Date(System.currentTimeMillis())));
    }

    private SagaEvent<PaymentStatusPayload> prepareSagaEvent(SagaEvent<?> sagaEvent) {
        SagaEvent<PaymentStatusPayload> PaymentStatusPayloadSagaEvent = new SagaEvent<>();

        PaymentStatusPayloadSagaEvent.setSagaId(sagaEvent.getSagaId());
        PaymentStatusPayloadSagaEvent.setServiceTransactionId("");
        PaymentStatusPayloadSagaEvent.setSagaStatus(SagaEvent.SagaStatus.FAILED);

        PaymentStatusPayloadSagaEvent.setEventOrigin(applicationName);
        PaymentStatusPayloadSagaEvent.setEventOrder(sagaEvent.getEventOrder() + 1);
        PaymentStatusPayloadSagaEvent.setTimestamp(DateFormat.getTimeInstance().format(new Date(System.currentTimeMillis())));

        return PaymentStatusPayloadSagaEvent;
    }
}
