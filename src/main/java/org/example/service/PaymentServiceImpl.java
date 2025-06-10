package org.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private final ConcurrentMap<String, Integer> payments = new ConcurrentHashMap<>();

    @Override
    public ResponseEntity<String> processPayment(String currency, int amount) {
        try {
            if (currency == null || currency.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Currency cannot be empty");
            }

            String normalizedCurrency = validateCurrency(currency);
            if (normalizedCurrency == null) {
                return ResponseEntity.badRequest()
                        .body("Invalid currency code. Please use ISO 4217 currency codes (e.g., USD, EUR)");
            }

            if (!isValidAmount(amount)) {
                return ResponseEntity.badRequest().body("Amount cannot be zero");
            }

            recordPayment(normalizedCurrency, amount);
            return ResponseEntity.ok("Payment recorded successfully");

        } catch (IllegalArgumentException e) {
            logger.error("Invalid input error: ", e);
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error processing payment: ", e);
            return ResponseEntity.internalServerError().body("Error processing payment");
        }
    }

    @Override
    public void logCurrentPayments() {
        try {
            logger.info("Current Payments:");
            payments.forEach((currency, amount) ->
                    logger.info("{}: {}", currency, amount));
        } catch (Exception e) {
            logger.error("Error logging payments: ", e);
        }
    }

    private String validateCurrency(String currency) {
        try {
            String normalizedCurrency = currency.toUpperCase().trim();
            Currency.getInstance(normalizedCurrency);
            return normalizedCurrency;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid currency code: {}", currency);
            return null;
        }
    }

    private boolean isValidAmount(int amount) {
        return amount != 0;
    }

    private void recordPayment(String currency, int amount) {
        payments.merge(currency, amount, Integer::sum);
    }
}