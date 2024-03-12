package com.samsungsds.msa.biz.payment.infrastructure;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="TB_PAYMENT")
@Data
public class PaymentDVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String payType;
    private int payAmount;
}
