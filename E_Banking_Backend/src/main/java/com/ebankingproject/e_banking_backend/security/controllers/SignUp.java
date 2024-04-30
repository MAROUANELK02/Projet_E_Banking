package com.ebankingproject.e_banking_backend.security.controllers;

import com.ebankingproject.e_banking_backend.security.models.User;
import com.ebankingproject.e_banking_backend.security.models.UserDTO;
import com.ebankingproject.e_banking_backend.security.payload.request.SignupChangePass;
import com.ebankingproject.e_banking_backend.security.payload.request.SignupCheck;
import com.ebankingproject.e_banking_backend.security.repository.UserRepository;
import com.ebankingproject.e_banking_backend.security.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*",maxAge = 3600)
@AllArgsConstructor
@RestController
@RequestMapping("/api/register")
public class SignUp {
    private AccountService accountService;
    private UserRepository userRepository;

    @PostMapping("/sendEmail")
    public int sendEmail(@RequestBody SignupCheck signupCheck) {
        User user = userRepository.findByEmailContains(signupCheck.getEmail());
        if(user != null) {
            return accountService.checkingEmail(signupCheck.getEmail());
        }else{
            throw new RuntimeException("Email inexistant !");
        }
    }

    @PutMapping("/changePassword")
    public UserDTO changePassword(@RequestBody SignupChangePass signupChangePass) {
        return accountService.changePassword(signupChangePass.getEmail(),signupChangePass.getPassword(),
                signupChangePass.getConfirmedPassword());
    }
}
