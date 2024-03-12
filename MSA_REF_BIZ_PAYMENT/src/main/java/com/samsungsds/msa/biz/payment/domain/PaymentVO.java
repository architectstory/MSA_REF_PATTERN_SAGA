package com.samsungsds.msa.biz.payment.domain;

import lombok.Data;

@Data
public class PaymentVO {
    private int id;
    private String payType;
    private int payAmount;
}
