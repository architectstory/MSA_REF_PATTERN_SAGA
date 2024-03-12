package com.samsungsds.msa.biz.payment.application;

import com.samsungsds.msa.biz.payment.domain.PaymentAggregate;
import com.samsungsds.msa.biz.payment.domain.PaymentVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private PaymentAggregate paymentAggregate;

    public PaymentServiceImpl(PaymentAggregate paymentAggregate){

        this.paymentAggregate = paymentAggregate;
    }

    @Override
    public void pay(PaymentDTO paymentDTO) {
        PaymentVO paymentVO = new PaymentVO();
        paymentVO.setPayType(paymentDTO.getPayType());
        paymentVO.setPayAmount(paymentDTO.getPayAmount());

        paymentAggregate.createPayment(paymentVO);
        paymentDTO.setId(paymentVO.getId());
    }
    @Override
    public List<PaymentDTO> payHistory() {
        List<PaymentDTO> paymentDTOList = new ArrayList<>();

        List<PaymentVO> paymentVOList= paymentAggregate.readPayment();
        for(PaymentVO paymentVO : paymentVOList){
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setId(paymentVO.getId());
            paymentDTO.setPayType(paymentVO.getPayType());
            paymentDTO.setPayAmount(paymentVO.getPayAmount());

            paymentDTOList.add(paymentDTO);
        }
        return paymentDTOList;
    }

    @Override
    public void cancelPayment(PaymentDTO paymentDTO) {
        PaymentVO paymentVO = new PaymentVO();
        paymentVO.setId(paymentDTO.getId());
        paymentVO.setPayType(paymentDTO.getPayType());
        paymentVO.setPayAmount(paymentDTO.getPayAmount());

        paymentAggregate.deletePayment(paymentVO);
    }
}
