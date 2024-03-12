package com.samsungsds.msa.saga.payload;

import lombok.Data;

@Data
public class OrderStatusPayload {
    private String orderId;
    private String orderOrigin;
    private String orderType;
    private int count;
    private int totalAmount;
    private String failedDescription;
}
