package com.samsungsds.msa.biz.payment.application;

import java.util.List;

public interface PaymentService {

    void pay(PaymentDTO paymentDTO);
    List<PaymentDTO> payHistory();
    void cancelPayment(PaymentDTO paymentDTO);
}
