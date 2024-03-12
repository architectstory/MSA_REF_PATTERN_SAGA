package com.samsungsds.msa.biz.order.application;

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
@RequestMapping("/rest/api/v1/order")
@Log4j2
public class OrderController {

    private final Logger logger = LogManager.getLogger(OrderController.class);

    @Autowired
    OrderService orderService;


    @PostMapping
    public void createOrder(OrderDTO orderDTO){
        orderService.createOrder(orderDTO);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> readOrder(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
        return new ResponseEntity<>(orderService.readOrder(), headers, HttpStatus.OK);
    }

    @PutMapping
    public void updateOrder(OrderDTO orderDTO){
        orderService.updateOrder(orderDTO);
    }

    @DeleteMapping
    public void deleteOrder(OrderDTO orderDTO){
        orderService.deleteOrder(orderDTO);
    }
}
