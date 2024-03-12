package com.samsungsds.msa.biz.order.infrastructure.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SagaEventHistoryH2Repository extends JpaRepository<SagaEventHistoryDVO, Long> {
}
