package com.samsungsds.msa.biz.order.application;

import lombok.Data;

@Data
public class OrderDTO {
    private int id;
    private String origin;
    private String type;
    private int count;
    private int cost;
}
