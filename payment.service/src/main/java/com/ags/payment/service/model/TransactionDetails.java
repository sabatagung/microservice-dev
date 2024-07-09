package com.ags.payment.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("transactionDetails")
public class TransactionDetails {
    @Id
    private Long id;
    private float amount;
    private Long order_id;
    private LocalDate payment_date;
    private String mode;
    private String status;
    private String reference_number;
}
