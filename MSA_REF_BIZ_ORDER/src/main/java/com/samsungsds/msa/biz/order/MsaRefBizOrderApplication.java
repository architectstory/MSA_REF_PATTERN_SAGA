package com.samsungsds.msa.biz.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.samsungsds.msa")
public class MsaRefBizOrderApplication {
    public static void main(String[] args){
        SpringApplication.run(MsaRefBizOrderApplication.class, args);
    }
}