package com.samsungsds.msa.biz.order.infrastructure.adapter;

import com.samsungsds.msa.biz.order.domain.OrderVO;
import com.samsungsds.msa.saga.event.SagaEvent;
import com.samsungsds.msa.saga.handler.ProducerSagaEventHandler;
import com.samsungsds.msa.saga.kafka.KafkaEventProcessor;
import com.samsungsds.msa.saga.payload.OrderStatusPayload;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;

@Component
public class OrderProducerSagaEventHandler implements ProducerSagaEventHandler<OrderVO> {

    Logger logger = LogManager.getLogger(OrderProducerSagaEventHandler.class);

    @Value("${spring.application.name}")
    String applicationName;

    @Value("${kafka.producer.order-success}")
    String orderSuccessTopic;

    @Autowired
    private KafkaEventProcessor kafkaEventProcessor;

    @Override
    public void publishEvent(OrderVO orderVO) {
        //saga event message
        SagaEvent<OrderStatusPayload> sagaEvent = new SagaEvent<>();
        sagaEvent.setSagaId(RandomStringUtils.randomNumeric(10));
        sagaEvent.setServiceTransactionId(String.valueOf(orderVO.getId()));
        sagaEvent.setEventOrigin(applicationName);
        sagaEvent.setEventType(orderSuccessTopic);
        sagaEvent.setSagaStatus(SagaEvent.SagaStatus.IN_PROGRESS);
        sagaEvent.setEventOrder(1);
        sagaEvent.setTimestamp(DateFormat.getDateInstance().format(new Date(System.currentTimeMillis())));

        //payload
        OrderStatusPayload orderStatusPayload = new OrderStatusPayload();
        orderStatusPayload.setOrderId(String.valueOf(orderVO.getId()));
        orderStatusPayload.setOrderOrigin(orderVO.getOrigin());
        orderStatusPayload.setCount(orderVO.getCount());
        orderStatusPayload.setTotalAmount(orderVO.getCount() * orderVO.getCost());

        sagaEvent.setPayload(orderStatusPayload);

        //publish
        kafkaEventProcessor.publish(orderSuccessTopic, sagaEvent);

        logger.debug("sagaEvent : " + sagaEvent + "  orderSuccessTopic " + orderSuccessTopic);
    }
}
