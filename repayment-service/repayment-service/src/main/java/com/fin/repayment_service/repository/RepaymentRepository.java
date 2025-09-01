package com.fin.repayment_service.repository;
import com.fin.repayment_service.entity.Repayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RepaymentRepository extends JpaRepository<Repayment, Long> {
    List<Repayment> findByCustomerId(Long customerId);
    List<Repayment> findByLoanApplicationId(Long loanApplicationId);
    List<Repayment> findByRepaymentStatus(String repaymentStatus);
    List<Repayment> findByNextPaymentDueDate(LocalDate nextPaymentDueDate);

    @Query("SELECT r FROM Repayment r WHERE r.nextPaymentDueDate < :date AND r.repaymentStatus != 'COMPLETED'")
    List<Repayment> findOverduePayments(@Param("date") LocalDate date);

    @Query("SELECT r FROM Repayment r WHERE r.customerId = :customerId AND r.repaymentStatus = 'PENDING' ORDER BY r.nextPaymentDueDate ASC")
    List<Repayment> findPendingPaymentsByCustomerId(@Param("customerId") Long customerId);
}