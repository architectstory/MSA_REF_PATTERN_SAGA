package com.samsungsds.msa.biz.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.samsungsds.msa")
public class MsaRefBizStockApplication {
    public static void main(String[] args){
        SpringApplication.run(MsaRefBizStockApplication.class, args);
    }
}