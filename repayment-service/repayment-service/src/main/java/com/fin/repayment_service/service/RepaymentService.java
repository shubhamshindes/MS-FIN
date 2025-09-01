package com.fin.repayment_service.service;
import com.fin.repayment_service.entity.Repayment;
import com.fin.repayment_service.repository.RepaymentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RepaymentService {

    private static final String REPAYMENT_SERVICE = "repaymentService";

    @Autowired
    private RepaymentRepository repaymentRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @CircuitBreaker(name = REPAYMENT_SERVICE, fallbackMethod = "fallbackCreateRepayment")
    public Repayment createRepayment(Repayment repayment) {
        Repayment savedRepayment = repaymentRepository.save(repayment);

        // Send notification via Kafka
        kafkaTemplate.send("repayment-created",
                "New repayment created for loan ID: " + savedRepayment.getLoanApplicationId() +
                        ", Amount: " + savedRepayment.getRepaymentAmount());

        return savedRepayment;
    }

    @CircuitBreaker(name = REPAYMENT_SERVICE, fallbackMethod = "fallbackGetAllRepayments")
    public List<Repayment> getAllRepayments() {
        return repaymentRepository.findAll();
    }

    @CircuitBreaker(name = REPAYMENT_SERVICE, fallbackMethod = "fallbackGetRepaymentById")
    public Optional<Repayment> getRepaymentById(Long id) {
        return repaymentRepository.findById(id);
    }

    @CircuitBreaker(name = REPAYMENT_SERVICE, fallbackMethod = "fallbackGetRepaymentsByCustomerId")
    public List<Repayment> getRepaymentsByCustomerId(Long customerId) {
        return repaymentRepository.findByCustomerId(customerId);
    }

    @CircuitBreaker(name = REPAYMENT_SERVICE, fallbackMethod = "fallbackGetRepaymentsByLoanApplicationId")
    public List<Repayment> getRepaymentsByLoanApplicationId(Long loanApplicationId) {
        return repaymentRepository.findByLoanApplicationId(loanApplicationId);
    }

    @CircuitBreaker(name = REPAYMENT_SERVICE, fallbackMethod = "fallbackUpdateRepaymentStatus")
    public Repayment updateRepaymentStatus(Long id, String status) {
        Repayment repayment = repaymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Repayment not found with id: " + id));

        repayment.setRepaymentStatus(status);

        // If payment is completed, send notification
        if ("COMPLETED".equals(status)) {
            kafkaTemplate.send("repayment-completed",
                    "Repayment completed for loan ID: " + repayment.getLoanApplicationId());
        }

        return repaymentRepository.save(repayment);
    }

    @CircuitBreaker(name = REPAYMENT_SERVICE, fallbackMethod = "fallbackGetPendingPaymentsByCustomerId")
    public List<Repayment> getPendingPaymentsByCustomerId(Long customerId) {
        return repaymentRepository.findPendingPaymentsByCustomerId(customerId);
    }

    // Fallback methods
    public Repayment fallbackCreateRepayment(Repayment repayment, Throwable t) {
        throw new RuntimeException("Repayment service is unavailable. Please try again later.");
    }

    public List<Repayment> fallbackGetAllRepayments(Throwable t) {
        throw new RuntimeException("Repayment service is unavailable. Please try again later.");
    }

    public Optional<Repayment> fallbackGetRepaymentById(Long id, Throwable t) {
        throw new RuntimeException("Repayment service is unavailable. Please try again later.");
    }

    public List<Repayment> fallbackGetRepaymentsByCustomerId(Long customerId, Throwable t) {
        throw new RuntimeException("Repayment service is unavailable. Please try again later.");
    }

    public List<Repayment> fallbackGetRepaymentsByLoanApplicationId(Long loanApplicationId, Throwable t) {
        throw new RuntimeException("Repayment service is unavailable. Please try again later.");
    }

    public Repayment fallbackUpdateRepaymentStatus(Long id, String status, Throwable t) {
        throw new RuntimeException("Repayment service is unavailable. Please try again later.");
    }

    public List<Repayment> fallbackGetPendingPaymentsByCustomerId(Long customerId, Throwable t) {
        throw new RuntimeException("Repayment service is unavailable. Please try again later.");
    }
}