package com.samsungsds.msa.biz.order.infrastructure;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="TB_ORDER")
@Data
public class OrderDVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String origin;
    private String type;
    private int count;
    private int cost;
}
