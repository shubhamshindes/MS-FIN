package com.fin.repayment_service.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "repayments")
public class Repayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repaymentId;

    @NotNull(message = "Loan application ID is required")
    @Column(nullable = false)
    private Long loanApplicationId;

    @NotNull(message = "Customer ID is required")
    @Column(nullable = false)
    private Long customerId;

    @NotNull(message = "Repayment amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Repayment amount must be greater than 0")
    @Column(nullable = false)
    private Double repaymentAmount;

    @NotNull(message = "Repayment date is required")
    @Column(nullable = false)
    private LocalDate repaymentDate;

    @NotNull(message = "Remaining balance is required")
    @Column(nullable = false)
    private Double remainingBalance;

    @NotBlank(message = "Repayment status is required")
    @Column(nullable = false, length = 50)
    private String repaymentStatus;

    @NotBlank(message = "Payment mode is required")
    @Column(nullable = false, length = 50)
    private String paymentMode;

    @NotNull(message = "Next payment due date is required")
    @Column(nullable = false)
    private LocalDate nextPaymentDueDate;
}