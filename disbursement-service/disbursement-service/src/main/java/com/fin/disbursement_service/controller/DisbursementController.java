package com.fin.disbursement_service.controller;

import com.fin.disbursement_service.entity.Disbursement;
import com.fin.disbursement_service.service.DisbursementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/disbursements")
public class DisbursementController {

    @Autowired
    private DisbursementService disbursementService;

    @PostMapping
    public ResponseEntity<Disbursement> createDisbursement(@Valid @RequestBody Disbursement disbursement) {
        Disbursement createdDisbursement = disbursementService.createDisbursement(disbursement);
        return new ResponseEntity<>(createdDisbursement, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Disbursement>> getAllDisbursements() {
        List<Disbursement> disbursements = disbursementService.getAllDisbursements();
        return new ResponseEntity<>(disbursements, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Disbursement> getDisbursementById(@PathVariable Long id) {
        Optional<Disbursement> disbursement = disbursementService.getDisbursementById(id);
        return disbursement.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Disbursement>> getDisbursementsByCustomerId(@PathVariable Long customerId) {
        List<Disbursement> disbursements = disbursementService.getDisbursementsByCustomerId(customerId);
        return new ResponseEntity<>(disbursements, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Disbursement> updateDisbursementStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String remarks) {
        Disbursement updatedDisbursement = disbursementService.updateDisbursementStatus(id, status, remarks);
        return new ResponseEntity<>(updatedDisbursement, HttpStatus.OK);
    }
}