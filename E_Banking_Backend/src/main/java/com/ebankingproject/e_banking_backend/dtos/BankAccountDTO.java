package com.ebankingproject.e_banking_backend.dtos;

import com.ebankingproject.e_banking_backend.enums.AccountStatus;
import lombok.Data;

import java.util.Date;

@Data
public class BankAccountDTO {
    private String id;
    private double balance;
    private String type;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private boolean closed;
}
