package org.example;

import org.example.controller.PaymentController;
import org.example.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        paymentController = new PaymentController(paymentService);
    }

    @Test
    void testValidCurrency() {
        when(paymentService.processPayment("USD", 100))
                .thenReturn(ResponseEntity.ok("Payment recorded successfully"));

        ResponseEntity<String> response = paymentController.recordPayment("USD", 100);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Payment recorded successfully", response.getBody());
    }

    @Test
    void testInvalidCurrencyCode() {
        when(paymentService.processPayment("USA", 100))
                .thenReturn(ResponseEntity.badRequest()
                        .body("Invalid currency code. Please use ISO 4217 currency codes (e.g., USD, EUR)"));

        ResponseEntity<String> response = paymentController.recordPayment("USA", 100);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid currency code. Please use ISO 4217 currency codes (e.g., USD, EUR)",
                response.getBody());
    }

    @Test
    void testNullCurrency() {
        when(paymentService.processPayment(null, 100))
                .thenReturn(ResponseEntity.badRequest().body("Currency cannot be empty"));

        ResponseEntity<String> response = paymentController.recordPayment(null, 100);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Currency cannot be empty", response.getBody());
    }

    @Test
    void testEmptyCurrency() {
        when(paymentService.processPayment("  ", 100))
                .thenReturn(ResponseEntity.badRequest().body("Currency cannot be empty"));

        ResponseEntity<String> response = paymentController.recordPayment("  ", 100);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Currency cannot be empty", response.getBody());
    }

    @Test
    void testZeroAmount() {
        when(paymentService.processPayment("USD", 0))
                .thenReturn(ResponseEntity.badRequest().body("Amount cannot be zero"));

        ResponseEntity<String> response = paymentController.recordPayment("USD", 0);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Amount cannot be zero", response.getBody());
    }

    @Test
    void testCurrencyNormalization() {
        when(paymentService.processPayment(anyString(), anyInt()))
                .thenReturn(ResponseEntity.ok("Payment recorded successfully"));

        ResponseEntity<String> response1 = paymentController.recordPayment("usd", 100);
        ResponseEntity<String> response2 = paymentController.recordPayment("USD", 100);

        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(HttpStatus.OK, response2.getStatusCode());
    }

    @Test
    void testLowercaseCurrency() {
        when(paymentService.processPayment("eur", 100))
                .thenReturn(ResponseEntity.ok("Payment recorded successfully"));

        ResponseEntity<String> response = paymentController.recordPayment("eur", 100);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Payment recorded successfully", response.getBody());
    }

    @Test
    void testCurrencyWithWhitespace() {
        when(paymentService.processPayment(" USD ", 100))
                .thenReturn(ResponseEntity.ok("Payment recorded successfully"));

        ResponseEntity<String> response = paymentController.recordPayment(" USD ", 100);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Payment recorded successfully", response.getBody());
    }

    @Test
    void testSpecialCharactersInCurrency() {
        when(paymentService.processPayment("US$", 100))
                .thenReturn(ResponseEntity.badRequest()
                        .body("Invalid currency code. Please use ISO 4217 currency codes (e.g., USD, EUR)"));

        ResponseEntity<String> response = paymentController.recordPayment("US$", 100);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid currency code"));
    }
}