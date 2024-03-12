package com.samsungsds.msa.biz.payment.application;

import lombok.Data;

@Data
public class PaymentDTO {
    private int id;
    private String payType;
    private int payAmount;
}
