package com.ebankingproject.e_banking_backend.web;

import com.ebankingproject.e_banking_backend.dtos.CustomerDTO;
import com.ebankingproject.e_banking_backend.exceptions.CustomerNotFoundException;
import com.ebankingproject.e_banking_backend.services.BankAccountService;
import lombok.AllArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/customers")
public class CustomerRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/listCustomers")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<CustomerDTO> customers() {
       return bankAccountService.listCustomer();
    }

    @GetMapping("/")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public Page<CustomerDTO> customerDTOS(@RequestParam(name = "page",defaultValue = "0") int page,
                                          @RequestParam(name = "size",defaultValue = "4") int size,
                                          @RequestParam(name = "keyword",defaultValue = "") String keyword) {
        return bankAccountService.pageCustomers(keyword,page, size);
    }

    @GetMapping("/{customerId}")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public CustomerDTO getCustomer(@PathVariable(name = "customerId")Long customerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(customerId);
    }

    /*@GetMapping("/user/{id}")
    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public CustomerDTO getCustomerByUserId(@PathVariable(name = "id")Long userId) {
        return bankAccountService.getCustomerByUserId(userId);
    }*/

    @PostMapping("/")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PutMapping("/{customerId}")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public CustomerDTO updateCustomer(@PathVariable(name = "customerId")Long customerId,@RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/{customerId}")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public void deleteCustomer(@PathVariable(name = "customerId")Long customerId) {
        bankAccountService.deleteCustomer(customerId);
    }
}
