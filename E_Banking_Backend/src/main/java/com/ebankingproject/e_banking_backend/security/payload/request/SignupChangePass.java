package com.ebankingproject.e_banking_backend.security.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupChangePass {
    private String email;
    private String password;
    private String ConfirmedPassword;
}
