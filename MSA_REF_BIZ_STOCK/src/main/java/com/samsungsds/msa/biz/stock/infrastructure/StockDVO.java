package com.samsungsds.msa.biz.stock.infrastructure;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="TB_STOCK")
@Data
public class StockDVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String origin;
//    private String type;
    private int count;
//    private int cost;
}
