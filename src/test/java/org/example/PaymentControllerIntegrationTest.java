package org.example;

import org.example.controller.PaymentController;
import org.junit.jupiter.api.Test;
import org.example.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    void whenValidInput_thenReturns200() throws Exception {
        when(paymentService.processPayment(anyString(), anyInt()))
                .thenReturn(ResponseEntity.ok("Payment recorded successfully"));

        mockMvc.perform(post("/payment/USD/100"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment recorded successfully"));
    }

    @Test
    void whenInvalidCurrency_thenReturns400() throws Exception {
        when(paymentService.processPayment("USA", 100))
                .thenReturn(ResponseEntity.badRequest()
                        .body("Invalid currency code. Please use ISO 4217 currency codes (e.g., USD, EUR)"));

        mockMvc.perform(post("/payment/USA/100"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid currency code. Please use ISO 4217 currency codes (e.g., USD, EUR)"));
    }

    @Test
    void whenZeroAmount_thenReturns400() throws Exception {
        when(paymentService.processPayment("USD", 0))
                .thenReturn(ResponseEntity.badRequest().body("Amount cannot be zero"));

        mockMvc.perform(post("/payment/USD/0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Amount cannot be zero"));
    }

    @Test
    void whenEmptyCurrency_thenReturns400() throws Exception {
        when(paymentService.processPayment(" ", 100))
                .thenReturn(ResponseEntity.badRequest().body("Currency cannot be empty"));

        mockMvc.perform(post("/payment/ /100"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Currency cannot be empty"));
    }

    @Test
    void whenLowercaseCurrency_thenReturns200() throws Exception {
        when(paymentService.processPayment("usd", 100))
                .thenReturn(ResponseEntity.ok("Payment recorded successfully"));

        mockMvc.perform(post("/payment/usd/100"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment recorded successfully"));
    }

    @Test
    void whenCurrencyWithWhitespace_thenReturns200() throws Exception {
        when(paymentService.processPayment(" USD ", 100))
                .thenReturn(ResponseEntity.ok("Payment recorded successfully"));

        mockMvc.perform(post("/payment/ USD /100"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment recorded successfully"));
    }

    @Test
    void whenInvalidPath_thenReturns404() throws Exception {
        mockMvc.perform(post("/invalid/USD/100"))
                .andExpect(status().isNotFound());
    }
}