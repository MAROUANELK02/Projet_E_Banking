package com.ebankingproject.e_banking_backend.web;

import com.ebankingproject.e_banking_backend.dtos.AccountOperationDTO;
import com.ebankingproject.e_banking_backend.exceptions.BalanceNotSufficientException;
import com.ebankingproject.e_banking_backend.exceptions.BankAccountNotFoundException;
import com.ebankingproject.e_banking_backend.exceptions.CustomerNotFoundException;
import com.ebankingproject.e_banking_backend.exceptions.OperationNotFoundException;
import com.ebankingproject.e_banking_backend.services.BankAccountService;
import lombok.AllArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/operations")
public class AccountOperationRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/listOperations")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<AccountOperationDTO> listOperations() {
        return bankAccountService.listOperations();
    }

    @GetMapping("/")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public Page<AccountOperationDTO> pageOperations(@RequestParam(name = "page",defaultValue = "0")int page,
                                                    @RequestParam(name = "size",defaultValue = "10")int size) {
        return bankAccountService.pageOperations(page, size);
    }

    @PostMapping("/transaction")
    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public void transaction(@RequestParam(name = "ribSource",defaultValue = "0") String idS,
                                @RequestParam(name = "ribDestination",defaultValue = "0")String idD,
                                @RequestParam(name = "amount",defaultValue = "0")double amount)
            throws BankAccountNotFoundException, BalanceNotSufficientException, CustomerNotFoundException {
        bankAccountService.transaction(idS,idD,amount);
    }

    @DeleteMapping("/{operationId}")
    //@PreAuthorize("hasRole('ADMIN')")
    public void deleteOperation(@PathVariable(name = "operationId")Long id) throws OperationNotFoundException, BankAccountNotFoundException {
        bankAccountService.deleteOperation(id);
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public AccountOperationDTO getOperation(@PathVariable(name = "id")Long id) throws OperationNotFoundException {
        return bankAccountService.getOperation(id);
    }

}
