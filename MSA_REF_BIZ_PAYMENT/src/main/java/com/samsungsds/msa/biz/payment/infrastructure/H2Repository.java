package com.samsungsds.msa.biz.payment.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface H2Repository extends JpaRepository<PaymentDVO, Long> {
}
