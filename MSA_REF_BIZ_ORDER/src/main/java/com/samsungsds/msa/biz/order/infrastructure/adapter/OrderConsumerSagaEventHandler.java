package com.samsungsds.msa.biz.order.infrastructure.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsungsds.msa.biz.order.application.OrderDTO;
import com.samsungsds.msa.biz.order.application.OrderService;
import com.samsungsds.msa.biz.order.domain.OrderVO;
import com.samsungsds.msa.saga.event.SagaEvent;
import com.samsungsds.msa.saga.handler.ConsumerSagaEventHandler;
import com.samsungsds.msa.saga.payload.OrderStatusPayload;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

import java.text.DateFormat;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;


@Component
public class OrderConsumerSagaEventHandler implements ConsumerSagaEventHandler<OrderVO> {
    Logger logger = LogManager.getLogger(OrderConsumerSagaEventHandler.class);

    @Value("${spring.application.name}")
    String applicationName;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SagaEventHistoryH2Repository sagaEventHistoryH2Repository;

    @Transactional
    @KafkaListener(topics = {"${kafka.consumer.order-success}"},
            groupId = "${kafka.consumer.group-id}")
    @Override
    public void transactionEvent(SagaEvent<?> sagaEvent) {
        logger.debug("=>>>>>>>>>>>>>>>>>>>>>>.." + this.getClass().getName() + " transactionEvent");
        saveSagaEventHistory(sagaEvent);

    }

    private void saveSagaEventHistory(SagaEvent<?> sagaEvent) {
        logger.debug("=>>>>>>>>>>>>>>>>>>>>>>.." + this.getClass().getName() + " saveSagaEventHistory");
        SagaEventHistoryDVO sagaEventHistoryDVO = new SagaEventHistoryDVO();
        sagaEventHistoryDVO.setSagaId(sagaEvent.getSagaId());
        sagaEventHistoryDVO.setEventOrigin(applicationName);
        sagaEventHistoryDVO.setServiceTransactionId("");
        sagaEventHistoryDVO.setSagaStatus(SagaEvent.SagaStatus.COMPLETED.toString());
        sagaEventHistoryDVO.setEventType("ORDER_COMPLETED");
        sagaEventHistoryDVO.setEventOrder(sagaEvent.getEventOrder() + 1);
        sagaEventHistoryDVO.setTimestamp(DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())));
        sagaEventHistoryH2Repository.save(sagaEventHistoryDVO);
    }


    @Transactional
    @KafkaListener(topics = {"${kafka.consumer.order-failed}"}, groupId = "${kafka.consumer.group-id}")
    @Override
    public void compensateEvent(SagaEvent<?> sagaEvent) {
        ObjectMapper objectMapper = new ObjectMapper();
        OrderStatusPayload orderStatusPayload = objectMapper.convertValue(sagaEvent.getPayload(), OrderStatusPayload.class);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(Integer.parseInt(orderStatusPayload.getOrderId()));
        orderService.deleteOrder(orderDTO);
        saveSagaEventHistoryForOrderFailed(sagaEvent);
    }
    private void saveSagaEventHistoryForOrderFailed(SagaEvent<?> sagaEvent) {
        SagaEventHistoryDVO sagaEventHistoryDVO = new SagaEventHistoryDVO();
        sagaEventHistoryDVO.setSagaId(sagaEvent.getSagaId());
        sagaEventHistoryDVO.setEventOrigin(applicationName);
        sagaEventHistoryDVO.setServiceTransactionId("");
        sagaEventHistoryDVO.setSagaStatus(SagaEvent.SagaStatus.FAILED.toString());
        sagaEventHistoryDVO.setEventType("ORDER_CANCELLED");
        sagaEventHistoryDVO.setEventOrder(sagaEvent.getEventOrder() + 1);
        sagaEventHistoryDVO.setTimestamp(sagaEvent.getTimestamp());
        sagaEventHistoryH2Repository.save(sagaEventHistoryDVO);
    }
}
