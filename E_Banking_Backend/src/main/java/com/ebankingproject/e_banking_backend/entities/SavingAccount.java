package com.ebankingproject.e_banking_backend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@DiscriminatorValue("SA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SavingAccount extends BankAccount{
    @NotEmpty
    private double interestRate;
}
