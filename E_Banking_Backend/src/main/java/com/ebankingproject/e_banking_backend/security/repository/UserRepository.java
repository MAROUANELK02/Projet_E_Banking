package com.ebankingproject.e_banking_backend.security.repository;

import com.ebankingproject.e_banking_backend.security.models.Role;
import com.ebankingproject.e_banking_backend.security.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    User findByEmailContains(String email);
    Boolean existsByEmail(String email);
    Page<User> findByRolesContains(Optional<Role> role, Pageable pageable);
    User findByCustomerId(Long customerId);
}
