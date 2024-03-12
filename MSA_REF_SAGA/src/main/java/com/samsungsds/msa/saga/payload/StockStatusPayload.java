package com.samsungsds.msa.saga.payload;

import lombok.Data;

@Data
public class StockStatusPayload {
    private int quantity;
    private String productOrigin;
    private String orderId;
    private String paymentId;
    private StockStatus stockStatus;
    private int count;
    private String orderOrigin;
    public enum StockStatus{
        AVAILABLE, OUTOFSTOCK
    }
}
