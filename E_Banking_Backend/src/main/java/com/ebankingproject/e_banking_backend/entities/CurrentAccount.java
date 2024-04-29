package com.ebankingproject.e_banking_backend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@DiscriminatorValue("CA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentAccount extends BankAccount{
    @NotNull
    private double overDraft;
}
