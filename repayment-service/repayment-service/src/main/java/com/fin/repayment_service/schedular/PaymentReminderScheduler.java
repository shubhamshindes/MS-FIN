package com.fin.repayment_service.schedular;
import com.fin.repayment_service.entity.Repayment;
import com.fin.repayment_service.repository.RepaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class PaymentReminderScheduler {

    @Autowired
    private RepaymentRepository repaymentRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // Run every day at 9 AM
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendPaymentReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Repayment> duePayments = repaymentRepository.findByNextPaymentDueDate(tomorrow);

        for (Repayment repayment : duePayments) {
            String message = String.format(
                    "Payment reminder: Loan ID %d has a payment due tomorrow. Amount: %.2f",
                    repayment.getLoanApplicationId(), repayment.getRepaymentAmount()
            );

            kafkaTemplate.send("payment-reminders", message);
        }
    }

    // Run every day at 10 AM for overdue payments
    @Scheduled(cron = "0 0 10 * * ?")
    public void sendOverduePaymentNotifications() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Repayment> overduePayments = repaymentRepository.findOverduePayments(yesterday);

        for (Repayment repayment : overduePayments) {
            String message = String.format(
                    "Overdue payment: Loan ID %d has an overdue payment. Amount: %.2f",
                    repayment.getLoanApplicationId(), repayment.getRepaymentAmount()
            );

            kafkaTemplate.send("overdue-payments", message);
        }
    }
}