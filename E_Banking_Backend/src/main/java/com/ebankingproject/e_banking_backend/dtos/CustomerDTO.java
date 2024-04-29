package com.ebankingproject.e_banking_backend.dtos;

import com.ebankingproject.e_banking_backend.enums.Ville;
import lombok.Data;

@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
    private String cin;
    private Ville ville;
    private boolean hasCurrentAccount;
    private boolean hasSavingAccount;
}
