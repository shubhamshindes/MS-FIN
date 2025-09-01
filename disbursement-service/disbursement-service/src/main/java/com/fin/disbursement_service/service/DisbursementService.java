package com.fin.disbursement_service.service;

import com.fin.disbursement_service.entity.Disbursement;
import com.fin.disbursement_service.repository.DisbursementRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DisbursementService {

    private static final String DISBURSEMENT_SERVICE = "disbursementService";

    @Autowired
    private DisbursementRepository disbursementRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @CircuitBreaker(name = DISBURSEMENT_SERVICE, fallbackMethod = "fallbackCreateDisbursement")
    public Disbursement createDisbursement(Disbursement disbursement) {
        Disbursement savedDisbursement = disbursementRepository.save(disbursement);

        // Send notification via Kafka
        kafkaTemplate.send("disbursement-created",
                "New disbursement created for loan ID: " + savedDisbursement.getLoanId() +
                        ", Amount: " + savedDisbursement.getDisbursedAmount());

        return savedDisbursement;
    }

    @CircuitBreaker(name = DISBURSEMENT_SERVICE, fallbackMethod = "fallbackGetAllDisbursements")
    public List<Disbursement> getAllDisbursements() {
        return disbursementRepository.findAll();
    }

    @CircuitBreaker(name = DISBURSEMENT_SERVICE, fallbackMethod = "fallbackGetDisbursementById")
    public Optional<Disbursement> getDisbursementById(Long id) {
        return disbursementRepository.findById(id);
    }

    @CircuitBreaker(name = DISBURSEMENT_SERVICE, fallbackMethod = "fallbackGetDisbursementsByCustomerId")
    public List<Disbursement> getDisbursementsByCustomerId(Long customerId) {
        return disbursementRepository.findByCustomerId(customerId);
    }

    @CircuitBreaker(name = DISBURSEMENT_SERVICE, fallbackMethod = "fallbackUpdateDisbursementStatus")
    public Disbursement updateDisbursementStatus(Long id, String status, String remarks) {
        Disbursement disbursement = disbursementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disbursement not found with id: " + id));

        disbursement.setStatus(status);
        disbursement.setRemarks(remarks);

        return disbursementRepository.save(disbursement);
    }

    // Fallback methods
    public Disbursement fallbackCreateDisbursement(Disbursement disbursement, Throwable t) {
        throw new RuntimeException("Disbursement service is unavailable. Please try again later.");
    }

    public List<Disbursement> fallbackGetAllDisbursements(Throwable t) {
        throw new RuntimeException("Disbursement service is unavailable. Please try again later.");
    }

    public Optional<Disbursement> fallbackGetDisbursementById(Long id, Throwable t) {
        throw new RuntimeException("Disbursement service is unavailable. Please try again later.");
    }

    public List<Disbursement> fallbackGetDisbursementsByCustomerId(Long customerId, Throwable t) {
        throw new RuntimeException("Disbursement service is unavailable. Please try again later.");
    }

    public Disbursement fallbackUpdateDisbursementStatus(Long id, String status, String remarks, Throwable t) {
        throw new RuntimeException("Disbursement service is unavailable. Please try again later.");
    }
}