package com.fin.disbursement_service.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "disbursements")
public class Disbursement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long disbursementId;

    @NotNull(message = "Loan ID is required")
    @Column(nullable = false)
    private Long loanId;

    @NotNull(message = "Customer ID is required")
    @Column(nullable = false)
    private Long customerId;

    @NotNull(message = "Disbursed amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Disbursed amount must be greater than 0")
    @Column(nullable = false)
    private BigDecimal disbursedAmount;

    @NotNull(message = "Disbursement date is required")
    @Column(nullable = false)
    private LocalDate disbursementDate;

    @NotBlank(message = "Payment method is required")
    @Column(nullable = false, length = 50)
    private String paymentMethod;

    @NotBlank(message = "Transaction reference is required")
    @Column(nullable = false, unique = true, length = 100)
    private String transactionReference;

    @NotBlank(message = "Status is required")
    @Column(nullable = false, length = 20)
    private String status;

    @Column(length = 255)
    private String remarks;

    @NotBlank(message = "Processed by is required")
    @Column(nullable = false, length = 50)
    private String processedBy;
}