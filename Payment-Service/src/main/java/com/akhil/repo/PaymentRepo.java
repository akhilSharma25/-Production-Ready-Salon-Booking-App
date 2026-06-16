package com.akhil.repo;

import com.akhil.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<PaymentOrder,Long> {
}
