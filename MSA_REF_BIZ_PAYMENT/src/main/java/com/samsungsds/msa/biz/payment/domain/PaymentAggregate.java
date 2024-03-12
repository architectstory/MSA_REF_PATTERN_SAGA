package com.samsungsds.msa.biz.payment.domain;


import java.util.List;

public class PaymentAggregate {

    private PaymentRepository paymentRepository;

    public PaymentAggregate(PaymentRepository paymentRepository){

        this.paymentRepository = paymentRepository;
    }

    public void createPayment(PaymentVO paymentVO){

        paymentRepository.createPayment(paymentVO);
    }

    public List<PaymentVO> readPayment() {

        return paymentRepository.readPayment();
    }

    public void deletePayment(PaymentVO paymentVO){
        paymentRepository.deletePayment(paymentVO);
    }

}