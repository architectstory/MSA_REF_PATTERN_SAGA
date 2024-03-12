package com.samsungsds.msa.biz.order.infrastructure.adapter;


import com.samsungsds.msa.saga.event.SagaEvent;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UpdateSagaEventHandler {
    Logger logger = LogManager.getLogger(UpdateSagaEventHandler.class);

    @Autowired
    private SagaEventHistoryH2Repository sagaEventHistoryH2Repository;

    @Transactional
    @KafkaListener(topics = {"${kafka.consumer.update-saga-event-topic}"}
            , groupId = "${kafka.consumer.group-id}")
    public void savaSagaEventHistoryListener(SagaEvent<?> event){
        logger.debug("sagaEvent : ====>>>>>>  " + event);
        savaSagaEventHistory(event);
    }

    private void savaSagaEventHistory(SagaEvent<?> sagaEvent){
        logger.debug("sagaEvent : ====>>>>>>  " + sagaEvent.getSagaStatus());
        SagaEventHistoryDVO sagaEventHistoryDVO = new SagaEventHistoryDVO();
        sagaEventHistoryDVO.setSagaId(sagaEvent.getSagaId());
        sagaEventHistoryDVO.setEventOrigin(sagaEvent.getEventOrigin());
        sagaEventHistoryDVO.setServiceTransactionId(sagaEvent.getServiceTransactionId());

        sagaEventHistoryDVO.setSagaStatus(sagaEvent.getSagaStatus().toString());
        sagaEventHistoryDVO.setEventType(sagaEvent.getEventType());
        sagaEventHistoryDVO.setEventOrder(sagaEvent.getEventOrder());
        sagaEventHistoryDVO.setTimestamp(sagaEvent.getTimestamp());
        sagaEventHistoryH2Repository.save(sagaEventHistoryDVO);
        logger.debug("=>>>>>>>>>>>>>>>>>>>>>>.." + this.getClass().getName() + " savaSagaEventHistoryListener");

    }
}
