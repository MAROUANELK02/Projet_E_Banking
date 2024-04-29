package com.ebankingproject.e_banking_backend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@DiscriminatorValue("SA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SavingAccount extends BankAccount{
    @NotNull
    private double interestRate;
}
