package com.samsungsds.msa.biz.payment.infrastructure;

import com.samsungsds.msa.biz.payment.domain.PaymentRepository;
import com.samsungsds.msa.biz.payment.domain.PaymentVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final Logger logger = LogManager.getLogger(PaymentRepositoryImpl.class);
   @Autowired
   H2Repository h2Repository;

    @Override
    public void createPayment(PaymentVO paymentVO) {
        PaymentDVO paymentDVO = new PaymentDVO();
        paymentDVO.setPayType(paymentVO.getPayType());
        paymentDVO.setPayAmount(paymentVO.getPayAmount());

        h2Repository.save(paymentDVO);
        paymentVO.setId(paymentDVO.getId());
    }

    @Override
    public List<PaymentVO> readPayment() {

        List<PaymentVO> paymentVOList = new ArrayList<>();
        List<PaymentDVO> paymentDVOList = h2Repository.findAll();
        for(PaymentDVO paymentDVO : paymentDVOList){
            PaymentVO paymentVO = new PaymentVO();
            paymentVO.setId(paymentDVO.getId());
            paymentVO.setPayType(paymentDVO.getPayType());
            paymentVO.setPayAmount(paymentDVO.getPayAmount());
            paymentVOList.add(paymentVO);
        }
        return paymentVOList;
    }
    @Override
    public void deletePayment(PaymentVO paymentVO) {
        PaymentDVO paymentDVO = new PaymentDVO();
        paymentDVO.setId(paymentVO.getId());
        paymentDVO.setPayType(paymentVO.getPayType());
        paymentDVO.setPayAmount(paymentVO.getPayAmount());
        h2Repository.delete(paymentDVO);
    }
}
