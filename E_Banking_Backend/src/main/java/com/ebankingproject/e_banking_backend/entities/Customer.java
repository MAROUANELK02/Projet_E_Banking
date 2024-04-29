package com.ebankingproject.e_banking_backend.entities;

import com.ebankingproject.e_banking_backend.enums.Ville;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String cin;
    @Enumerated(EnumType.STRING)
    private Ville ville;
    @OneToMany(mappedBy = "customer")
    private List<BankAccount> bankAccounts;
    @Value("${hasCurrentAccount:false}")
    private boolean hasCurrentAccount;
    @Value("${hasSavingAccount:false}")
    private boolean hasSavingAccount;
//    @OneToOne(fetch = FetchType.LAZY)
//    private User user;
}
