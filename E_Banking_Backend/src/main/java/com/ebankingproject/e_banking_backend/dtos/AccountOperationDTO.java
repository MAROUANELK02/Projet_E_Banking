package com.ebankingproject.e_banking_backend.dtos;

import com.ebankingproject.e_banking_backend.enums.OperationType;
import lombok.Data;

import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;
    private String bankAccountDTOId;
    private boolean canceled;
}
