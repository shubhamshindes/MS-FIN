package com.fin.repayment_service.controller;
import com.fin.repayment_service.entity.Repayment;
import com.fin.repayment_service.service.RepaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/repayments")
public class RepaymentController {

    @Autowired
    private RepaymentService repaymentService;

    @PostMapping
    public ResponseEntity<Repayment> createRepayment(@Valid @RequestBody Repayment repayment) {
        Repayment createdRepayment = repaymentService.createRepayment(repayment);
        return new ResponseEntity<>(createdRepayment, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Repayment>> getAllRepayments() {
        List<Repayment> repayments = repaymentService.getAllRepayments();
        return new ResponseEntity<>(repayments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Repayment> getRepaymentById(@PathVariable Long id) {
        Optional<Repayment> repayment = repaymentService.getRepaymentById(id);
        return repayment.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Repayment>> getRepaymentsByCustomerId(@PathVariable Long customerId) {
        List<Repayment> repayments = repaymentService.getRepaymentsByCustomerId(customerId);
        return new ResponseEntity<>(repayments, HttpStatus.OK);
    }

    @GetMapping("/loan/{loanApplicationId}")
    public ResponseEntity<List<Repayment>> getRepaymentsByLoanApplicationId(@PathVariable Long loanApplicationId) {
        List<Repayment> repayments = repaymentService.getRepaymentsByLoanApplicationId(loanApplicationId);
        return new ResponseEntity<>(repayments, HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}/pending")
    public ResponseEntity<List<Repayment>> getPendingPaymentsByCustomerId(@PathVariable Long customerId) {
        List<Repayment> repayments = repaymentService.getPendingPaymentsByCustomerId(customerId);
        return new ResponseEntity<>(repayments, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Repayment> updateRepaymentStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        Repayment updatedRepayment = repaymentService.updateRepaymentStatus(id, status);
        return new ResponseEntity<>(updatedRepayment, HttpStatus.OK);
    }
}