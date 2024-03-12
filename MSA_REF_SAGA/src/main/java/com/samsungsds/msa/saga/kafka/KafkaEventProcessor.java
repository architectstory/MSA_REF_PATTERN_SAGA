package com.samsungsds.msa.saga.kafka;

import com.samsungsds.msa.saga.event.EventProcessor;
import com.samsungsds.msa.saga.event.SagaEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventProcessor implements EventProcessor {

    @Autowired
    private KafkaTemplate<String, SagaEvent<?>> kafkaTemplate;

    @Override
    public void publish(String topicName, SagaEvent<?> eventMessage) {
        kafkaTemplate.send(topicName, eventMessage);
        kafkaTemplate.send("update-saga-event", eventMessage);
    }
}
