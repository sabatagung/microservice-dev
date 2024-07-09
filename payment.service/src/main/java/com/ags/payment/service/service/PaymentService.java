package com.ags.payment.service.service;

import com.ags.payment.service.dto.PaymentDTO;
import com.ags.payment.service.dto.PaymentRespDTO;
import com.ags.payment.service.model.Payment;
import com.ags.payment.service.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Mono<PaymentRespDTO> createBalance(PaymentDTO paymentDTO) {
        log.info("{} - Creating balance for payment: {}", PaymentService.class.getSimpleName(), paymentDTO);

        Payment payment = Payment.builder()
                .customer_id(paymentDTO.getCustomer_id())
                .balance(paymentDTO.getBalance())
                .build();

        return paymentRepository.save(payment)
                .flatMap(savedPayment -> {
                    log.info("{} - Balance created successfully for customer: {}",
                            PaymentService.class.getSimpleName(),
                            savedPayment.getCustomer_id());

                    if (savedPayment.getId() == null) {
                        return Mono.error(new RuntimeException("Failed to generate ID for the payment"));
                    }

                    return Mono.just(PaymentRespDTO.builder()
                            .id(savedPayment.getId())
                            .customer_id(savedPayment.getCustomer_id())
                            .balance(savedPayment.getBalance())
                            .build());
                });
    }
    public Flux<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll()
                .map(payment -> PaymentDTO.builder()
                        .customer_id(payment.getCustomer_id())
                        .balance(payment.getBalance())
                        .build());
    }
}
