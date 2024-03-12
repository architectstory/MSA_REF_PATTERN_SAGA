package com.samsungsds.msa.biz.stock.domain;

import com.samsungsds.msa.biz.stock.MsaRefBizStockApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = MsaRefBizStockApplication.class)
public class DomainBeanConfiguration {
    @Bean
    StockAggregate stockAggregate(StockRepository stockRepository){
        return new StockAggregate(stockRepository);
    }
    @Bean
    StockVO stockVO(){
        return new StockVO();
    }
}
