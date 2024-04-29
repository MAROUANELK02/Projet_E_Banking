package com.ebankingproject.e_banking_backend.entities;

import com.ebankingproject.e_banking_backend.enums.OperationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "operations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountOperation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date operationDate;

    @NotNull
    private double amount;

    @Enumerated(EnumType.STRING)
    private OperationType type;

    @ManyToOne
    private BankAccount bankAccount;

    private String description;

    @Value("${canceled:false}")
    private boolean canceled;
}
