package com.ebankingproject.e_banking_backend.services;

import com.ebankingproject.e_banking_backend.dtos.*;
import com.ebankingproject.e_banking_backend.exceptions.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BankAccountService {

    //Customers Transactions

    List<CustomerDTO> listCustomer();
    Page<CustomerDTO> pageCustomers(String keyword, int page, int size);
    CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;
    CustomerDTO getCustomerByUserId(Long userId);
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(CustomerDTO customerDTO) throws CustomerNotFoundException;
    void deleteCustomer(Long customerId);

    //BankAccount Transactions

    List<BankAccountDTO> bankAccountList();
    List<BankAccountDTO> bankAccountByCustomer(Long Id);
    Page<CurrentBankAccountDTO> pageCurrentAccounts(int page, int size);
    Page<SavingBankAccountDTO> pageSavingAccounts(int page, int size);
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    CurrentBankAccountDTO saveCurrentBankAccount(double overDraft, Long customerId) throws CustomerNotFoundException, hasAlreadyCurrentAccount;
    SavingBankAccountDTO saveSavingBankAccount(double interestRate, Long customerId) throws CustomerNotFoundException, hasAlreadySavingAccount;
    void debit(String accountId,double amount,String description) throws BankAccountNotFoundException, RuntimeException;
    void credit(String accountId,double amount,String description) throws BankAccountNotFoundException;
    void transaction(String accountIdSource,String accountIdDestination,double amount) throws BalanceNotSufficientException, BankAccountNotFoundException, CustomerNotFoundException;
    void deleteBankAccount(String accountId) throws BankAccountNotFoundException;
    Page<AccountOperationDTO> accountHistory(String id,int page,int size);
    BankAccountDTO suspendAccount(String id) throws BankAccountNotFoundException;
    BankAccountDTO activateAccount(String id) throws BankAccountNotFoundException;
    CurrentBankAccountDTO getCurrentAccount(Long customerId);
    SavingBankAccountDTO getSavingAccount(Long customerId);

    //Account Operations

    List<AccountOperationDTO> listOperations();
    Page<AccountOperationDTO> pageOperations(int page,int size);
    AccountOperationDTO getOperation(Long id) throws OperationNotFoundException;
    void deleteOperation(Long id) throws OperationNotFoundException, BankAccountNotFoundException;

}
