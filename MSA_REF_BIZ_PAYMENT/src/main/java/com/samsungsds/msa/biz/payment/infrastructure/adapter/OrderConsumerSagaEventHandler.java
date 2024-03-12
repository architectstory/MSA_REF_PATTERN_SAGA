package com.samsungsds.msa.biz.payment.infrastructure.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsungsds.msa.biz.payment.application.PaymentDTO;
import com.samsungsds.msa.biz.payment.application.PaymentService;
import com.samsungsds.msa.saga.event.SagaEvent;
import com.samsungsds.msa.saga.handler.ConsumerSagaEventHandler;
import com.samsungsds.msa.saga.kafka.KafkaEventProcessor;
import com.samsungsds.msa.saga.payload.OrderStatusPayload;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;


@Component
public class OrderConsumerSagaEventHandler implements ConsumerSagaEventHandler<SagaEvent<?>> {

    Logger logger = LogManager.getLogger(OrderConsumerSagaEventHandler.class);

    @Value("${spring.application.name}")
    String applicationName;

    @Value("${kafka.producer.order-success-topic}")
    private String orderSuccessTopic;

    @Value("${kafka.producer.order-failed-topic}")
    private String orderFailedTopic;

    @Autowired
    private KafkaEventProcessor kafkaEventProcessor;

    @Autowired
    private PaymentProducerSagaEventHandler paymentProducerSagaEventHandler;

    @Autowired
    private final PaymentService paymentService;

    public OrderConsumerSagaEventHandler(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @Transactional
    @KafkaListener(topics = {"${kafka.consumer.pub-order-success}"}, groupId = "${kafka.consumer.group-id}")
    @Override
    public void transactionEvent(SagaEvent<?> sagaEvent) {
        ObjectMapper objectMapper = new ObjectMapper();
        OrderStatusPayload orderStatusPayload = objectMapper.convertValue(sagaEvent.getPayload(), OrderStatusPayload.class);

        logger.debug("=>>>>>>>>>>>>>>>>>>>>>>.." + this.getClass().getName() + " transactionEvent" + " orderStatusPayload : " + orderStatusPayload);

        PaymentDTO paymentDTO = new PaymentDTO();
        int totalAmount = orderStatusPayload.getTotalAmount();
        if(totalAmount < 0) {
            SagaEvent<OrderStatusPayload> orderStatusPayloadSagaEvent = prepareSagaEvent(sagaEvent);

            //compensate logic
            compensateEvent(orderStatusPayloadSagaEvent);
        }
        else {
            //payment
            paymentDTO.setPayType("CreditCard");
            paymentDTO.setPayAmount(orderStatusPayload.getTotalAmount());

            logger.debug(paymentDTO.getPayType());
            logger.debug(paymentDTO.getPayAmount());

            paymentService.pay(paymentDTO);

            //payment publish
            sagaEvent.setServiceTransactionId(String.valueOf(paymentDTO.getId()));
            paymentProducerSagaEventHandler.publishEvent(sagaEvent);
        }

    }

    @Override
    public void compensateEvent(SagaEvent<?> sagaEvent) {
        sagaEvent.setEventType(orderFailedTopic);
        kafkaEventProcessor.publish(orderFailedTopic, sagaEvent);
    }

    private SagaEvent<OrderStatusPayload> prepareSagaEvent(SagaEvent<?> sagaEvent) {
        SagaEvent<OrderStatusPayload> orderStatusPayloadSagaEvent = new SagaEvent<>();
        orderStatusPayloadSagaEvent.setSagaId(sagaEvent.getSagaId());
        orderStatusPayloadSagaEvent.setServiceTransactionId("");
        orderStatusPayloadSagaEvent.setSagaStatus(SagaEvent.SagaStatus.FAILED);
        orderStatusPayloadSagaEvent.setEventType(orderFailedTopic);
        orderStatusPayloadSagaEvent.setEventOrigin(applicationName);
        orderStatusPayloadSagaEvent.setEventOrder(sagaEvent.getEventOrder() + 1);
        orderStatusPayloadSagaEvent.setTimestamp(DateFormat.getTimeInstance().format(new Date(System.currentTimeMillis())));

        ObjectMapper objectMapper = new ObjectMapper();
        OrderStatusPayload orderStatusPayloadEvent = objectMapper.convertValue(sagaEvent.getPayload(), OrderStatusPayload.class);

        OrderStatusPayload orderStatusPayload = new OrderStatusPayload();
        orderStatusPayload.setOrderId(orderStatusPayloadEvent.getOrderId());
        orderStatusPayload.setFailedDescription("your have been exceed your daily amount limit");

        orderStatusPayloadSagaEvent.setPayload(orderStatusPayload);

        return orderStatusPayloadSagaEvent;
    }

}
