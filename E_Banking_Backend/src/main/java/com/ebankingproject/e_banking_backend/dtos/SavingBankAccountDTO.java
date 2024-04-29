package com.ebankingproject.e_banking_backend.dtos;

import lombok.Data;

@Data
public class SavingBankAccountDTO extends BankAccountDTO{
    private double interestRate;
}
