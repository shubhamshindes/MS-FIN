package com.fin.customer_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @NotBlank(message = "Customer name is required")
    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Pattern(regexp = "^[+]*[(]?[0-9]{1,4}[)]?[-\\s\\./0-9]*$", message = "Phone number is not valid")
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @NotBlank(message = "National identity number is required")
    @Column(name = "national_identity_number", nullable = false, unique = true, length = 20)
    private String nationalIdentityNumber;

    @NotBlank(message = "Address is required")
    @Column(nullable = false)
    private String address;

    @NotNull(message = "Date of birth is required")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "account_status", nullable = false, length = 20)
    private String accountStatus = "ACTIVE";
}