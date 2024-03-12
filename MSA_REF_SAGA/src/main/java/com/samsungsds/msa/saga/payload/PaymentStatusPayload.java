package com.samsungsds.msa.saga.payload;

import lombok.Data;

@Data
public class PaymentStatusPayload {
    private String orderId;
    private String paymentId;
    private String orderOrigin;
    private PaymentStatus paymentStatus;
    private int count;

    public enum PaymentStatus{
        SUCCESS, FAILED
    }
    private String paymentDescription;
}
