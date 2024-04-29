package com.ebankingproject.e_banking_backend.repositories;

import com.ebankingproject.e_banking_backend.entities.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
    Page<AccountOperation> findByBankAccountIdAndCanceledFalse(String id, Pageable pageable);
    Page<AccountOperation> findAllByCanceledFalse(Pageable pageable);
}
