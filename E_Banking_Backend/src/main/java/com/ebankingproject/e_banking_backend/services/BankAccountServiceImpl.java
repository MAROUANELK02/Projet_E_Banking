package com.ebankingproject.e_banking_backend.services;

import com.ebankingproject.e_banking_backend.dtos.*;
import com.ebankingproject.e_banking_backend.entities.*;
import com.ebankingproject.e_banking_backend.enums.*;
import com.ebankingproject.e_banking_backend.exceptions.*;
import com.ebankingproject.e_banking_backend.mappers.BankAccountMapperImpl;
import com.ebankingproject.e_banking_backend.repositories.*;
import com.ebankingproject.e_banking_backend.security.models.ERole;
import com.ebankingproject.e_banking_backend.security.models.User;
import com.ebankingproject.e_banking_backend.security.services.AccountService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements  BankAccountService{

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private CurrentBankAccountRepository currentBankAccountRepository;
    private SavingBankAccountRepository savingBankAccountRepository;
    private BankAccountMapperImpl dtoMapper;
    private AccountService accountService;
    private PasswordEncoder passwordEncoder;

    //Customers Transactions

    @Override
    public List<CustomerDTO> listCustomer() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
    }

    @Override
    public Page<CustomerDTO> pageCustomers(String keyword, int page, int size) {
        Page<Customer> customers = customerRepository.findByFirstNameContainingOrLastNameContaining(keyword,keyword,
                PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"id")));
        return customers.map(customer -> dtoMapper.fromCustomer(customer));
    }

    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {
        return dtoMapper.fromCustomer(customerRepository.findById(id)
                .orElseThrow(()->new CustomerNotFoundException("Customer not found")));
    }

    @Override
    public CustomerDTO getCustomerByUserId(Long id) {
        return dtoMapper.fromCustomer(customerRepository.findByUserId(id));
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        String pw = passwordEncoder.encode(UUID.randomUUID().toString());
        User user = accountService.addNewUser(new User(customerDTO.getFirstName()+"_"+customerDTO.getLastName(),customerDTO.getEmail(),pw));
        accountService.addRoleToUser(user.getUsername(), ERole.ROLE_USER);
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        customer.setUser(user);
        user.setCustomer(customer);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerDTO.getId()).orElseThrow(()->new CustomerNotFoundException("Customer not found"));
        accountService.updateUser(customer.getId(),customerDTO.getFirstName()+"_"+customerDTO.getLastName(),customerDTO.getEmail());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        customer.setCin(customerDTO.getCin());
        customer.setVille(customerDTO.getVille());
        customerRepository.save(customer);
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        accountService.deleteUserByCustomerId(customerId);
        customerRepository.deleteById(customerId);
    }

    //BankAccount Transactions

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        return bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount savingAccount) {
                return dtoMapper.fromSavingAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentAccount(currentAccount);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public List<BankAccountDTO> bankAccountByCustomer(Long Id) {
        List<BankAccount> bankAccounts = bankAccountRepository.findByCustomerId(Id);
        return bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount savingAccount) {
                return dtoMapper.fromSavingAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentAccount(currentAccount);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public Page<CurrentBankAccountDTO> pageCurrentAccounts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC,"createdAt"));
        Page<CurrentAccount> currentAccounts = currentBankAccountRepository.findAllByClosedFalse(pageable);
        return currentAccounts.map(currentAccount -> dtoMapper.fromCurrentAccount(currentAccount));
    }

    @Override
    public Page<SavingBankAccountDTO> pageSavingAccounts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC,"createdAt"));
        Page<SavingAccount> savingAccounts = savingBankAccountRepository.findAllByClosedFalse(pageable);
        return savingAccounts.map(savingAccount -> dtoMapper.fromSavingAccount(savingAccount));
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        if(bankAccount instanceof CurrentAccount currentAccount) {
            return dtoMapper.fromCurrentAccount(currentAccount);
        }
        else {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingAccount(savingAccount);
        }
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double overDraft, Long customerId) throws CustomerNotFoundException, hasAlreadyCurrentAccount {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null)
            throw new CustomerNotFoundException("Customer not found");
        if(customer.isHasCurrentAccount())
            throw new hasAlreadyCurrentAccount("The customer has already a current account !");
        CurrentAccount currentAccount = new CurrentAccount();
        String randomString = String.valueOf((long) (Math.random() * 9_000_000_000_000_000L) + 1_000_000_000_000_000L);
        if(customer.getVille().equals(Ville.CASABLANCA)) {
            currentAccount.setId("123"+"780"+randomString);
        } else if (customer.getVille().equals(Ville.RABAT)) {
            currentAccount.setId("123"+"785"+randomString);
        } else if (customer.getVille().equals(Ville.TANGER)) {
            currentAccount.setId("123"+"790"+randomString);
        } else if (customer.getVille().equals(Ville.MARRAKECH)) {
            currentAccount.setId("123"+"795"+randomString);
        }
        currentAccount.setCreatedAt(new Date());
        currentAccount.setStatus(AccountStatus.ACTIVATED);
        customer.setHasCurrentAccount(true);
        customerRepository.save(customer);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);

        return dtoMapper.fromCurrentAccount(bankAccountRepository.save(currentAccount));
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double interestRate, Long customerId) throws CustomerNotFoundException, hasAlreadySavingAccount {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null)
            throw new CustomerNotFoundException("Customer not found");
        if(customer.isHasSavingAccount())
            throw new hasAlreadySavingAccount("The customer has already a saving account !");
        SavingAccount savingAccount = new SavingAccount();
        String randomString = String.valueOf((long) (Math.random() * 9_000_000_000_000_000L) + 1_000_000_000_000_000L);
        if(customer.getVille().equals(Ville.CASABLANCA)) {
            savingAccount.setId("123"+"780"+randomString);
        } else if (customer.getVille().equals(Ville.RABAT)) {
            savingAccount.setId("123"+"785"+randomString);
        } else if (customer.getVille().equals(Ville.TANGER)) {
            savingAccount.setId("123"+"790"+randomString);
        } else if (customer.getVille().equals(Ville.MARRAKECH)) {
            savingAccount.setId("123"+"795"+randomString);
        }
        savingAccount.setCreatedAt(new Date());
        savingAccount.setStatus(AccountStatus.ACTIVATED);
        customer.setHasSavingAccount(true);
        customerRepository.save(customer);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        return dtoMapper.fromSavingAccount(bankAccountRepository.save(savingAccount));
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
        try{
            if(bankAccount.getStatus().equals(AccountStatus.ACTIVATED)) {
                if(amount > 0) {
                    if(bankAccount.getBalance()<amount) {
                        throw new BalanceNotSufficientException("Solde insuffisant");
                    }
                    else{
                        AccountOperation accountOperation = new AccountOperation();
                        accountOperation.setType(OperationType.DEBIT);
                        accountOperation.setDescription(description);
                        accountOperation.setAmount(amount);
                        accountOperation.setOperationDate(new Date());
                        accountOperation.setBankAccount(bankAccount);
                        accountOperationRepository.save(accountOperation);
                        bankAccount.setBalance(bankAccount.getBalance()-amount);
                        bankAccountRepository.save(bankAccount);
                    }
                }else{
                    throw new RuntimeException("Vous devez saisir un montant positif !");
                }
            }else{
                throw new RuntimeException("Le compte à débiter n'est pas activé.");
            }
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
        try{
            if(bankAccount.getStatus().equals(AccountStatus.ACTIVATED)) {
                if(amount > 0) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setType(OperationType.CREDIT);
                    accountOperation.setAmount(amount);
                    accountOperation.setDescription(description);
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setBankAccount(bankAccount);
                    accountOperationRepository.save(accountOperation);
                    bankAccount.setBalance(bankAccount.getBalance()+amount);
                    bankAccountRepository.save(bankAccount);
                }else{
                    throw new RuntimeException("Vous devez saisir un montant positif !");
                }
            }else{
                throw new RuntimeException(" Le compte à créditer n'est pas activé.");
            }
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void transaction(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, CustomerNotFoundException {
        if(amount >= 100) {
            debit(accountIdSource, amount, "Virement envoyé à " +
                    getCustomer(getBankAccount(accountIdDestination).getCustomerDTO().getId()).getFirstName()
                    + " " +
                    getCustomer(getBankAccount(accountIdDestination).getCustomerDTO().getId()).getLastName());
            credit(accountIdDestination, amount, "Virement reçu de " +
                    getCustomer(getBankAccount(accountIdSource).getCustomerDTO().getId()).getFirstName()
                    + " " +
                    getCustomer(getBankAccount(accountIdSource).getCustomerDTO().getId()).getLastName());
        }else{
            throw new RuntimeException("Le montant doit être supérieur ou égal à 100DHS !");
        }
    }

    @Override
    public void deleteBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccountDTO bankAccountDTO = getBankAccount(accountId);
        BankAccount bankAccount;
        Customer customer = customerRepository.findById(bankAccountDTO.getCustomerDTO().getId()).orElseThrow();
        if(bankAccountDTO instanceof CurrentBankAccountDTO) {
            bankAccount = dtoMapper.fromCurrentBankAccountDTO((CurrentBankAccountDTO) bankAccountDTO);
            customer.setHasCurrentAccount(false);
        }else{
            bankAccount = dtoMapper.fromSavingBankAccountDTO((SavingBankAccountDTO) bankAccountDTO);
            customer.setHasSavingAccount(false);
        }
        customerRepository.save(customer);
        bankAccount.setClosed(true);
        bankAccount.setStatus(AccountStatus.SUSPENDED);
        bankAccount.setCustomer(customer);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public Page<AccountOperationDTO> accountHistory(String id,int page,int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC,"id"));
        Page <AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdAndCanceledFalse(id,pageable);
        return accountOperations.map(accountOperation -> dtoMapper.fromAccountOperation(accountOperation));
    }

    @Override
    public BankAccountDTO suspendAccount(String id) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(id).orElseThrow(() -> new BankAccountNotFoundException("BankAccount not Found"));
        bankAccount.setStatus(AccountStatus.SUSPENDED);
        if(bankAccount instanceof CurrentAccount)
            return dtoMapper.fromCurrentAccount((CurrentAccount) bankAccount);
        else
            return dtoMapper.fromSavingAccount((SavingAccount) bankAccount);
    }

    @Override
    public BankAccountDTO activateAccount(String id) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(id).orElseThrow(() -> new BankAccountNotFoundException("BankAccount not Found"));
        bankAccount.setStatus(AccountStatus.ACTIVATED);
        if(bankAccount instanceof CurrentAccount)
            return dtoMapper.fromCurrentAccount((CurrentAccount) bankAccount);
        else
            return dtoMapper.fromSavingAccount((SavingAccount) bankAccount);
    }


    //Account Operations

    @Override
    public List<AccountOperationDTO> listOperations(){
        return accountOperationRepository.findAll().stream()
                .map(accountOperation -> dtoMapper.fromAccountOperation(accountOperation))
                .collect(Collectors.toList());
    }

    @Override
    public Page<AccountOperationDTO> pageOperations(int page,int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"id"));
        Page<AccountOperation> accountOperations = accountOperationRepository.findAllByCanceledFalse(pageable);
        return accountOperations.map(accountOperation -> dtoMapper.fromAccountOperation(accountOperation));
    }

    @Override
    public void deleteOperation(Long id) throws BankAccountNotFoundException {
        AccountOperation accountOperation = accountOperationRepository.findById(id).orElseThrow();
        if(accountOperation.getDescription().startsWith("Retrait")) {
            accountOperation.setCanceled(true);
            accountOperationRepository.save(accountOperation);
            credit(accountOperation.getBankAccount().getId(), accountOperation.getAmount(),"Opération de remboursement");
        }else if(accountOperation.getDescription().startsWith("Versement")) {
            accountOperation.setCanceled(true);
            accountOperationRepository.save(accountOperation);
            debit(accountOperation.getBankAccount().getId(),accountOperation.getAmount(),"Opération de débit rectificatif");
        } else if (accountOperation.getDescription().startsWith("Opération")) {
            throw new RuntimeException("Cette opération ne peut pas être annulée");
        } else {
            throw new RuntimeException("Vous ne pouvez pas annuler un virement");
        }
    }

    @Override
    public CurrentBankAccountDTO getCurrentAccount(Long customerId) {
        return dtoMapper.fromCurrentAccount(currentBankAccountRepository.findByCustomerIdAndClosedFalse(customerId));
    }

    @Override
    public SavingBankAccountDTO getSavingAccount(Long customerId) {
        return dtoMapper.fromSavingAccount(savingBankAccountRepository.findByCustomerIdAndClosedFalse(customerId));
    }

    @Override
    public AccountOperationDTO getOperation(Long id) throws OperationNotFoundException {
        return dtoMapper.fromAccountOperation(accountOperationRepository.findById(id)
                .orElseThrow(()->new OperationNotFoundException("Operation not found")));
    }

}
