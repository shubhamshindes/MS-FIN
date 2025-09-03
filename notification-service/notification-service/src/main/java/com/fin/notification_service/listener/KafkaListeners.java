package com.fin.notification_service.listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @Autowired
    private JavaMailSender emailSender;

    @KafkaListener(topics = "customer-created", groupId = "notification-group")
    public void listenCustomerCreated(String message) {
        System.out.println("Received message: " + message);
        sendEmail("shindeshubham8055@gmail.com", "New Customer Created", message);
    }

    @KafkaListener(topics = "customer-deleted", groupId = "notification-group")
    public void listenCustomerDeleted(String message) {
        System.out.println("Received message: " + message);
        sendEmail("admin@loanapp.com", "Customer Deleted", message);
    }

    @KafkaListener(topics = "disbursement-created", groupId = "notification-group")
    public void listenDisbursementCreated(String message) {
        System.out.println("Received message: " + message);
        sendEmail("admin@loanapp.com", "New Disbursement Created", message);
    }

    @KafkaListener(topics = "payment-reminders", groupId = "notification-group")
    public void listenPaymentReminders(String message) {
        System.out.println("Received payment reminder: " + message);
        // In a real application, you would send this to the specific customer
        sendEmail("customers@loanapp.com", "Payment Reminder", message);
    }

    @KafkaListener(topics = "overdue-payments", groupId = "notification-group")
    public void listenOverduePayments(String message) {
        System.out.println("Received overdue payment notification: " + message);
        // In a real application, you would send this to the specific customer
        sendEmail("customers@loanapp.com", "Overdue Payment Notification", message);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}