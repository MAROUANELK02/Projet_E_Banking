package com.ebankingproject.e_banking_backend.mappers;

import com.ebankingproject.e_banking_backend.dtos.*;
import com.ebankingproject.e_banking_backend.entities.*;

public interface BankAccountMapper {
    public CustomerDTO fromCustomer(Customer customer);

    public Customer fromCustomerDTO(CustomerDTO customerDTO);

    public SavingBankAccountDTO fromSavingAccount(SavingAccount savingAccount);

    public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO savingBankAccountDTO);

    public CurrentBankAccountDTO fromCurrentAccount(CurrentAccount currentAccount);

    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO currentBankAccountDTO);

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation);

    public AccountOperation fromAccountOperationDTO(AccountOperationDTO accountOperationDTO);
}
