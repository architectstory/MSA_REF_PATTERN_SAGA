package com.samsungsds.msa.biz.payment.infrastructure.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsungsds.msa.biz.payment.application.PaymentDTO;
import com.samsungsds.msa.biz.payment.application.PaymentService;
import com.samsungsds.msa.saga.event.SagaEvent;
import com.samsungsds.msa.saga.handler.ConsumerSagaEventHandler;
import com.samsungsds.msa.saga.kafka.KafkaEventProcessor;
import com.samsungsds.msa.saga.payload.OrderStatusPayload;
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

    @Autowired
    private KafkaEventProcessor kafkaEventProcessor;

    @Autowired
    private OrderProducerSagaEventHandler orderProducerSagaEventHandler;

    @Autowired
    private OrderConsumerSagaEventHandler orderConsumerSagaEventHandler;

    @Autowired
    private final PaymentService paymentService;

    public PaymentConsumerSagaEventHandler(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @Transactional
    @KafkaListener(topics = {"${kafka.consumer.payment-success-topic}"},
            groupId = "${kafka.consumer.group-id}")
    @Override
    public void transactionEvent(SagaEvent<?> sagaEvent) {
        orderProducerSagaEventHandler.publishEvent(sagaEvent);
    }

    @Transactional
    @KafkaListener(topics = {"${kafka.consumer.payment-failed-topic}"},
            groupId = "${kafka.consumer.group-id}")
    @Override
    public void compensateEvent(SagaEvent<?> sagaEvent) {
        ObjectMapper objectMapper = new ObjectMapper();

        PaymentStatusPayload paymentStatusPayload = objectMapper.convertValue(sagaEvent, PaymentStatusPayload.class);

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setId(Integer.parseInt(paymentStatusPayload.getPaymentId()));
        paymentService.cancelPayment(paymentDTO);

        SagaEvent<OrderStatusPayload> orderStatusPayloadSagaEvent = new SagaEvent<>();
        orderStatusPayloadSagaEvent.setSagaId(sagaEvent.getSagaId());
        orderStatusPayloadSagaEvent.setServiceTransactionId("");
        orderStatusPayloadSagaEvent.setSagaStatus(SagaEvent.SagaStatus.FAILED);
        orderStatusPayloadSagaEvent.setEventOrder(sagaEvent.getEventOrder() + 1);
        orderStatusPayloadSagaEvent.setEventOrigin(applicationName);
        orderStatusPayloadSagaEvent.setTimestamp(DateFormat.getTimeInstance().format(new Date(System.currentTimeMillis())));

        OrderStatusPayload orderStatusPayload = new OrderStatusPayload();
        orderStatusPayload.setOrderId(paymentStatusPayload.getOrderId());
        orderStatusPayload.setFailedDescription(paymentStatusPayload.getPaymentDescription());

        orderStatusPayloadSagaEvent.setPayload(orderStatusPayload);

        orderConsumerSagaEventHandler.compensateEvent(orderStatusPayloadSagaEvent);

    }

}
