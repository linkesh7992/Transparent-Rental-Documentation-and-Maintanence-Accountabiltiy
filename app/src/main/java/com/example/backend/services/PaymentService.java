package com.example.backend.services;

import com.example.backend.models.PaymentRecord;
import com.example.backend.models.PaymentStatus;
import com.example.backend.models.PaymentType;
import com.example.backend.repository.RentWiseRepository;
import java.util.ArrayList;
import java.util.List;

public class PaymentService {
    private final RentWiseRepository repository;

    public PaymentService() {
        this.repository = RentWiseRepository.getInstance();
    }

    public double calculateOutstandingBalance() {
        double total = 0;
        for (PaymentRecord p : repository.getPaymentRecords()) {
            if (p.getPaymentStatus() == PaymentStatus.PENDING) {
                total += p.getAmount();
            }
        }
        return total;
    }

    public double calculateTotalPaid() {
        double total = 0;
        for (PaymentRecord p : repository.getPaymentRecords()) {
            if (p.getPaymentStatus() == PaymentStatus.SUCCESSFUL) {
                total += p.getAmount();
            }
        }
        return total;
    }

    public List<PaymentRecord> filterPayments(PaymentStatus status, PaymentType type) {
        List<PaymentRecord> list = new ArrayList<>();
        for (PaymentRecord p : repository.getPaymentRecords()) {
            boolean matchesStatus = (status == null) || (p.getPaymentStatus() == status);
            boolean matchesType = (type == null) || (p.getPaymentType() == type);
            if (matchesStatus && matchesType) {
                list.add(p);
            }
        }
        return list;
    }
}
