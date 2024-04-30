package com.ebankingproject.e_banking_backend.security.services;

import com.ebankingproject.e_banking_backend.security.models.ERole;
import com.ebankingproject.e_banking_backend.security.models.Role;
import com.ebankingproject.e_banking_backend.security.models.User;
import com.ebankingproject.e_banking_backend.security.models.UserDTO;
import org.springframework.data.domain.Page;


public interface AccountService {
    User addNewUser(User user);
    void addNewRole(Role role);
    void addRoleToUser(String username, ERole eRole);
    User loadUserByUsername(String username);
    User addNewModerator(String username, String email, String password);
    Page<UserDTO> findModerators(int page, int size);
    void deleteModUser(Long userId);
    int generateCode();
    int sendMail(String toEmail);

    void deleteUserByCustomerId(Long customerId);
    void updateUser(Long id,String username,String email);

    //check mail to change password
    int checkingEmail(String address);
    UserDTO changePassword(String email,String password,String confirmedPassword);
}
