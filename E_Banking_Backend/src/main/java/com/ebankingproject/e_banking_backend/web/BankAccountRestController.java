package com.ebankingproject.e_banking_backend.web;

import com.ebankingproject.e_banking_backend.dtos.*;
import com.ebankingproject.e_banking_backend.exceptions.*;
import com.ebankingproject.e_banking_backend.services.BankAccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*",maxAge = 3600)
@RequestMapping("/api/accounts")
public class BankAccountRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/customer/{customerId}")
    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<BankAccountDTO> getAccounts(@PathVariable("customerId") Long id) {
        return bankAccountService.bankAccountByCustomer(id);
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public BankAccountDTO getAccount(@PathVariable("id") String id) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(id);
    }

    @GetMapping("/current")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public Page<CurrentBankAccountDTO> currentAccountDTOS(@RequestParam(name = "page",defaultValue = "0") int page,
                                                          @RequestParam(name = "size",defaultValue = "4") int size) {
        return bankAccountService.pageCurrentAccounts(page,size);
    }

    @GetMapping("/saving")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public Page<SavingBankAccountDTO> savingAccountDTOS(@RequestParam(name = "page",defaultValue = "0") int page,
                                                        @RequestParam(name = "size",defaultValue = "4") int size) {
        return bankAccountService.pageSavingAccounts(page,size);
    }

    @GetMapping("/{accountId}/operations")
    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public Page<AccountOperationDTO> getHistory(@PathVariable(name = "accountId")String id,
                                                @RequestParam(name = "page",defaultValue = "0")int page,
                                                @RequestParam(name = "size",defaultValue = "6")int size) {
        return bankAccountService.accountHistory(id,page,size);
    }

    @PostMapping("/create/currentAccount/{customerId}")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public BankAccountDTO saveCurrentAccount(@RequestBody @Valid CurrentBankAccountDTO currentBankAccountDTO,
                                             @PathVariable(name = "customerId")Long customerId) throws CustomerNotFoundException, hasAlreadyCurrentAccount {
        return bankAccountService.saveCurrentBankAccount(currentBankAccountDTO.getOverDraft(),customerId);
    }

    @PostMapping("/create/savingAccount/{id}")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public BankAccountDTO saveSavingAccount(@RequestBody @Valid SavingBankAccountDTO savingBankAccountDTO,
                                            @PathVariable(name = "id")Long customerId) throws CustomerNotFoundException, hasAlreadySavingAccount {
        return bankAccountService.saveSavingBankAccount(savingBankAccountDTO.getInterestRate(),customerId);
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public void deleteBankAccount(@PathVariable("id") String id) throws BankAccountNotFoundException {
        bankAccountService.deleteBankAccount(id);
    }

    @PutMapping("/{id}/suspended")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public BankAccountDTO suspendBankAccount(@PathVariable("id") String id) throws BankAccountNotFoundException {
        return bankAccountService.suspendAccount(id);
    }

    @PutMapping("/{id}/activated")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public BankAccountDTO activateBankAccount(@PathVariable("id") String id) throws BankAccountNotFoundException {
        return bankAccountService.activateAccount(id);
    }

    @PostMapping("/debit")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public DebitDTO debit(@RequestBody @Valid DebitDTO debitDTO) throws BankAccountNotFoundException {
            this.bankAccountService.debit(debitDTO.getAccountId(),debitDTO.getAmount(),"Retrait du compte");
            return debitDTO;
    }

    @PostMapping("/credit")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public CreditDTO credit(@RequestBody @Valid CreditDTO creditDTO) throws BankAccountNotFoundException {
        this.bankAccountService.credit(creditDTO.getAccountId(),creditDTO.getAmount(),"Versement sur compte");
        return creditDTO;
    }
}
