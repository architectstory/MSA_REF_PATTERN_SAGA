package com.samsungsds.msa.biz.order.application;

import com.samsungsds.msa.biz.order.domain.OrderAggregate;
import com.samsungsds.msa.biz.order.domain.OrderVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderAggregate orderAggregate;

    public OrderServiceImpl(OrderAggregate orderAggregate){
        this.orderAggregate = orderAggregate;
    }

    @Override
    public void createOrder(OrderDTO orderDTO) {
        OrderVO orderVO = new OrderVO();
        orderVO.setId(orderDTO.getId());
        orderVO.setOrigin(orderDTO.getOrigin());
        orderVO.setType(orderDTO.getType());
        orderVO.setCount(orderDTO.getCount());
        orderVO.setCost(orderDTO.getCost());
        orderAggregate.createOrder(orderVO);
    }
    @Override
    public List<OrderDTO> readOrder() {
        List<OrderDTO> orderDTOList = new ArrayList<>();

        List<OrderVO> orderVOList= orderAggregate.readOrder();
        for(OrderVO orderVO : orderVOList){
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(orderVO.getId());
            orderDTO.setOrigin(orderVO.getOrigin());
            orderDTO.setType(orderVO.getType());
            orderDTO.setCount(orderVO.getCount());
            orderDTO.setCost(orderVO.getCost() * 5);

            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }
    @Override
    public void updateOrder(OrderDTO orderDTO) {
        OrderVO orderVO = new OrderVO();
        orderVO.setId(orderDTO.getId());
        orderVO.setOrigin(orderDTO.getOrigin());
        orderVO.setType(orderDTO.getType());
        orderVO.setCount(orderDTO.getCount());
        orderVO.setCost(orderDTO.getCost());
        orderAggregate.updateOrder(orderVO);
    }

    @Override
    public void deleteOrder(OrderDTO orderDTO) {
        OrderVO orderVO = new OrderVO();
        orderVO.setId(orderDTO.getId());
        orderAggregate.deleteOrder(orderVO);
    }

}
