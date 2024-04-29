package com.ebankingproject.e_banking_backend.repositories;

import com.ebankingproject.e_banking_backend.entities.SavingAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingBankAccountRepository extends JpaRepository<SavingAccount,String> {
    Page<SavingAccount> findAllByClosedFalse(Pageable pageable);
}
