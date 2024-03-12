package com.samsungsds.msa.biz.order.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface H2Repository extends JpaRepository<OrderDVO, Long> {
}
