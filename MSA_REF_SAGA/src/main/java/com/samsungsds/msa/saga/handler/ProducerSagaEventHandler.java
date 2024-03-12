package com.samsungsds.msa.saga.handler;

public interface ProducerSagaEventHandler<T> {
    public void publishEvent(T event);
}
