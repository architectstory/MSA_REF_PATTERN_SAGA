package com.samsungsds.msa.biz.payment.domain;

import java.util.List;

public interface PaymentRepository {
    void createPayment(PaymentVO paymentVO);

    List<PaymentVO> readPayment();

    void deletePayment(PaymentVO paymentVO);
}
