package com.samsungsds.msa.biz.order.domain;

import com.samsungsds.msa.biz.order.MsaRefBizOrderApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = MsaRefBizOrderApplication.class)
public class DomainBeanConfiguration {
    @Bean
    OrderAggregate orderAggregate(OrderRepository orderRepository){
        return new OrderAggregate(orderRepository);
    }
    @Bean
    OrderVO orderVO(){
        return new OrderVO();
    }
}
