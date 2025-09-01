package com.fin.disbursement_service.repository;
import com.fin.disbursement_service.entity.Disbursement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisbursementRepository extends JpaRepository<Disbursement, Long> {
    List<Disbursement> findByCustomerId(Long customerId);
    List<Disbursement> findByStatus(String status);
    List<Disbursement> findByLoanId(Long loanId);
    boolean existsByTransactionReference(String transactionReference);
}
