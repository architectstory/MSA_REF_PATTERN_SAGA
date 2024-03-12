package com.samsungsds.msa.saga.event;

import lombok.Data;

@Data
public class SagaEvent<T> {
    private String sagaId;
    private String serviceTransactionId;
    private String eventType;
    private T payload;
    private String timestamp;
    private int eventOrder;
    private SagaStatus sagaStatus;

    public enum SagaStatus {
        IN_PROGRESS, FAILED, COMPLETED
    }
    private String eventOrigin;
}
