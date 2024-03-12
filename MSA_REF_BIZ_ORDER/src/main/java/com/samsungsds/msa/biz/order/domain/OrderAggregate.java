package com.samsungsds.msa.biz.order.domain;


import java.util.List;

public class OrderAggregate {

    private OrderRepository orderRepository;

    public OrderAggregate(OrderRepository orderRepository){

        this.orderRepository = orderRepository;
    }

    public void createOrder(OrderVO orderVO){

        checkStock(orderVO);
        payOrder(orderVO);

        orderRepository.createOrder(orderVO);
    }
    private boolean checkStock(OrderVO orderVO) {
        return true;
    }
    private boolean payOrder(OrderVO orderVO) {
        return true;
    }

    public List<OrderVO> readOrder() {

        return orderRepository.readOrder();
    }

    public void updateOrder(OrderVO orderVO){
        System.out.println("OrderAggregate::updateOrder");
        orderRepository.updateOrder(orderVO);
    }

    public void deleteOrder(OrderVO orderVO){
        orderRepository.deleteOrder(orderVO);
    }

}