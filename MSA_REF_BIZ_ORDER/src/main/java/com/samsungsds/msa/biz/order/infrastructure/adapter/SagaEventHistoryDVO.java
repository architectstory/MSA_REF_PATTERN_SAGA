package com.samsungsds.msa.biz.order.infrastructure.adapter;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="TB_SAGA_EVENT_HISTORY")
@Setter
@Getter
public class SagaEventHistoryDVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String sagaId;
    private String eventOrigin;
    private String serviceTransactionId;
    private String sagaStatus;
    private String eventType;
    private int eventOrder;
    private String timestamp;
}
