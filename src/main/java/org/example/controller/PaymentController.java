package org.example.controller;

import org.example.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{currency}/{amount}")
    public ResponseEntity<String> recordPayment(@PathVariable String currency, @PathVariable int amount) {
        return paymentService.processPayment(currency, amount);
    }

    @Scheduled(fixedRate = 6000)
    public void logPayments() {
        paymentService.logCurrentPayments();
    }

}