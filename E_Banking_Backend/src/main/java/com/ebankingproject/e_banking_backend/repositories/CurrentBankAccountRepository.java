package com.ebankingproject.e_banking_backend.repositories;

import com.ebankingproject.e_banking_backend.entities.CurrentAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentBankAccountRepository extends JpaRepository<CurrentAccount,String> {
    Page<CurrentAccount> findAllByClosedFalse(Pageable pageable);
    CurrentAccount findByCustomerIdAndClosedFalse(Long customerId);
}
