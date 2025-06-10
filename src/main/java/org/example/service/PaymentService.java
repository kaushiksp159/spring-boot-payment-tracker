package org.example.service;

import org.springframework.http.ResponseEntity;

public interface PaymentService {
    ResponseEntity<String> processPayment(String currency, int amount);
    void logCurrentPayments();
}