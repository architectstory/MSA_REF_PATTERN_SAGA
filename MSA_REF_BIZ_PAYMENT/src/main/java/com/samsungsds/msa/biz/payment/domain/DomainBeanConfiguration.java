package com.samsungsds.msa.biz.payment.domain;

import com.samsungsds.msa.biz.payment.MsaRefBizPaymentApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = MsaRefBizPaymentApplication.class)
public class DomainBeanConfiguration {
    @Bean
    PaymentAggregate paymentAggregate(PaymentRepository paymentRepository){
        return new PaymentAggregate(paymentRepository);
    }
    @Bean
    PaymentVO paymentVO(){
        return new PaymentVO();
    }
}
