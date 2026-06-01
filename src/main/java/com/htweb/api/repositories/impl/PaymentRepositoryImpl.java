package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.PaymentRepository;
import com.htweb.core.pojo.Payment;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository("apiPaymentRepository")
public class PaymentRepositoryImpl extends BaseRepositoryImpl<Payment, Long> implements PaymentRepository {
    public PaymentRepositoryImpl() {
        super(Payment.class);
    }
}
