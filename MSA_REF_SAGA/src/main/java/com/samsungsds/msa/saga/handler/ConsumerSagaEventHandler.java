package com.samsungsds.msa.saga.handler;

import com.samsungsds.msa.saga.event.SagaEvent;

public interface ConsumerSagaEventHandler<O> {
    public void transactionEvent(SagaEvent<?> event);

    public void compensateEvent(SagaEvent<?> event);
}
