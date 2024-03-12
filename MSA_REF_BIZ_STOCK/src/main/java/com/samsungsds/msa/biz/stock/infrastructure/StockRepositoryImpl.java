package com.samsungsds.msa.biz.stock.infrastructure;

import com.samsungsds.msa.biz.stock.domain.StockRepository;
import com.samsungsds.msa.biz.stock.domain.StockVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StockRepositoryImpl implements StockRepository {

    private final Logger logger = LogManager.getLogger(StockRepositoryImpl.class);
   @Autowired
   H2Repository h2Repository;

    @Override
    public void createStock(StockVO stockVO) {
        StockDVO stockDVO = new StockDVO();
//        stockDVO.setType(stockVO.getType());
        stockDVO.setOrigin(stockVO.getOrigin());
        stockDVO.setCount(stockVO.getCount());
        h2Repository.save(stockDVO);
        stockVO.setId(stockDVO.getId());
    }

    @Override
    public List<StockVO> readStock() {

        List<StockVO> stockVOList = new ArrayList<>();
        List<StockDVO> stockDVOList = h2Repository.findAll();
        for(StockDVO stockDVO : stockDVOList){
            StockVO stockVO = new StockVO();
            stockVO.setId(stockDVO.getId());
//            stockVO.setType(stockDVO.getType());
            stockVO.setOrigin(stockDVO.getOrigin());
            stockVO.setCount(stockDVO.getCount());

            stockVOList.add(stockVO);
        }
        return stockVOList;
    }

    @Override
    public void updateStock(StockVO stockVO) {
        StockDVO stockDVO = new StockDVO();
        stockDVO.setId(stockVO.getId());
        stockDVO.setOrigin(stockVO.getOrigin());
//        stockDVO.setType(stockVO.getType());
        stockDVO.setCount(stockVO.getCount());
        h2Repository.save(stockDVO);
    }

    @Override
    public void deleteStock(StockVO stockVO) {
        StockDVO stockDVO = new StockDVO();
        stockDVO.setId(stockVO.getId());
        stockDVO.setOrigin(stockVO.getOrigin());
//        stockDVO.setType(stockVO.getType());
        stockDVO.setCount(stockVO.getCount());
        h2Repository.delete(stockDVO);
    }
}
