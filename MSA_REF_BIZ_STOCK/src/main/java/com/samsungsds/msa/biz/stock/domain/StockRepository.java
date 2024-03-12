package com.samsungsds.msa.biz.stock.domain;

import java.util.List;

public interface StockRepository {
    void createStock(StockVO stockVO);

    List<StockVO> readStock();

    void updateStock(StockVO stockVO);

    void deleteStock(StockVO stockVO);
}
