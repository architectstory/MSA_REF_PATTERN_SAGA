package com.samsungsds.msa.biz.payment.application;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.List;

@RestController
@RequestMapping("/rest/api/v1/payment")
@Log4j2
public class PaymentController {

    private final Logger logger = LogManager.getLogger(PaymentController.class);

    @Autowired
    PaymentService paymentService;


    @PostMapping
    public void pay(PaymentDTO paymentDTO){
        paymentService.pay(paymentDTO);
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> payHistory(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
        return new ResponseEntity<>(paymentService.payHistory(), headers, HttpStatus.OK);
    }


    @DeleteMapping
    public void cancelPayment(PaymentDTO paymentDTO){
        paymentService.cancelPayment(paymentDTO);
    }
}
