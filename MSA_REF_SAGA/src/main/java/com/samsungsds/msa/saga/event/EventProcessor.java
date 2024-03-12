package com.samsungsds.msa.saga.event;

public interface EventProcessor {
    void publish(String topicName, SagaEvent<?> sagaEvent);
}
