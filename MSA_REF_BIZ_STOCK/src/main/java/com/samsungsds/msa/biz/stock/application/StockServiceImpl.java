package com.samsungsds.msa.biz.stock.application;

import com.samsungsds.msa.biz.stock.domain.StockAggregate;
import com.samsungsds.msa.biz.stock.domain.StockVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    private StockAggregate stockAggregate;

    public StockServiceImpl(StockAggregate stockAggregate){
        this.stockAggregate = stockAggregate;
    }

    @Override
    public void createStock(StockDTO stockDTO) {
        StockVO stockVO = new StockVO();
        stockVO.setId(stockDTO.getId());
        stockVO.setOrigin(stockDTO.getOrigin());
//        stockVO.setType(stockDTO.getType());
        stockVO.setCount(stockDTO.getCount());
//        stockVO.setCost(stockDTO.getCost());
        stockAggregate.createStock(stockVO);
    }
    @Override
    public List<StockDTO> readStock() {
        List<StockDTO> stockDTOList = new ArrayList<>();

        List<StockVO> orderVOList= stockAggregate.readStock();
        for(StockVO stockVO : orderVOList){
            StockDTO stockDTO = new StockDTO();
            stockDTO.setId(stockVO.getId());
            stockDTO.setOrigin(stockVO.getOrigin());
//            stockDTO.setType(stockVO.getType());
            stockDTO.setCount(stockVO.getCount());
//            stockDTO.setCost(stockVO.getCost());

            stockDTOList.add(stockDTO);
        }
        return stockDTOList;
    }
    @Override
    public void updateStock(StockDTO stockDTO) {
        StockVO stockVO = getStockVO(stockDTO);
        stockVO.setId(stockDTO.getId());
        stockAggregate.updateStock(stockVO);
    }

    @Override
    public void deleteStock(StockDTO stockDTO) {
        StockVO stockVO = getStockVO(stockDTO);
        stockVO.setId(stockDTO.getId());
        stockAggregate.deleteStock(stockVO);
    }

    private StockVO getStockVO(StockDTO stockDTO){
        StockVO stockVO = new StockVO();
        stockVO.setOrigin(stockDTO.getOrigin());
//        stockVO.setType(stockDTO.getType());
        stockVO.setCount(stockDTO.getCount());
//        stockVO.setCost(stockDTO.getCost());
        return stockVO;
    }


}
