package com.samsungsds.msa.biz.stock.application;

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
@RequestMapping("/rest/api/v1/stock")
@Log4j2
public class StockController {

    private final Logger logger = LogManager.getLogger(StockController.class);

    @Autowired
    StockService stockService;


    @PostMapping
    public void createStock(StockDTO stockDTO){
        stockService.createStock(stockDTO);
    }

    @GetMapping
    public ResponseEntity<List<StockDTO>> readOrder(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
        return new ResponseEntity<>(stockService.readStock(), headers, HttpStatus.OK);
    }

    @PutMapping
    public void updateStock(StockDTO stockDTO){
        stockService.updateStock(stockDTO);
    }

    @DeleteMapping
    public void deleteStock(StockDTO stockDTO){
        stockService.deleteStock(stockDTO);
    }
}
