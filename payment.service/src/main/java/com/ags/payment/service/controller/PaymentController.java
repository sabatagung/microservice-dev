package com.ags.payment.service.controller;

import com.ags.payment.service.dto.PaymentDTO;
import com.ags.payment.service.dto.PaymentRespDTO;
import com.ags.payment.service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping ("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public Mono<PaymentRespDTO> createPayment(@RequestBody PaymentDTO paymentDTO) {
        return paymentService.createBalance(paymentDTO);
    }

    @GetMapping("/all")
    public Flux<PaymentDTO> getAllPayments() {
        return paymentService.getAllPayments();
    }
}
