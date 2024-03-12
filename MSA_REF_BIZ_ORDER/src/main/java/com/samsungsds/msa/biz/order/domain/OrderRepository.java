package com.samsungsds.msa.biz.order.domain;

import java.util.List;

public interface OrderRepository {
    void createOrder(OrderVO orderVO);

    List<OrderVO> readOrder();

    void updateOrder(OrderVO orderVO);

    void deleteOrder(OrderVO orderVO);
}
