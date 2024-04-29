package com.ebankingproject.e_banking_backend.entities;

import com.ebankingproject.e_banking_backend.enums.Ville;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3,max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 3,max = 50)
    private String lastName;

    @Email
    private String email;

    @NotBlank
    @Column(unique = true)
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
