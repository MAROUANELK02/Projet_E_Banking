package com.ebankingproject.e_banking_backend.security.repository;

import com.ebankingproject.e_banking_backend.security.models.ERole;
import com.ebankingproject.e_banking_backend.security.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
