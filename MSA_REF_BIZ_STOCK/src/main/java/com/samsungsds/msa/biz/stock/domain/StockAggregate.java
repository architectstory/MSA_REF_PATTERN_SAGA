package com.samsungsds.msa.biz.stock.domain;


import java.util.List;

public class StockAggregate {

    private StockRepository stockRepository;

    public StockAggregate(StockRepository stockRepository){

        this.stockRepository = stockRepository;
    }

    public void createStock(StockVO stockVO){

        stockRepository.createStock(stockVO);
    }

    public List<StockVO> readStock() {

        return stockRepository.readStock();
    }

    public void updateStock(StockVO stockVO){
        System.out.println("StockAggregate::updateStock");
        stockRepository.updateStock(stockVO);
    }

    public void deleteStock(StockVO stockVO){
        stockRepository.deleteStock(stockVO);
    }

}