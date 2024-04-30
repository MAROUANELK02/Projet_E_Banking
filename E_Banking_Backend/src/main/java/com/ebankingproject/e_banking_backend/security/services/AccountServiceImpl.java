package com.ebankingproject.e_banking_backend.security.services;

import com.ebankingproject.e_banking_backend.Email.EmailSenderService;
import com.ebankingproject.e_banking_backend.security.mappers.UserMapperImpl;
import com.ebankingproject.e_banking_backend.security.models.ERole;
import com.ebankingproject.e_banking_backend.security.models.Role;
import com.ebankingproject.e_banking_backend.security.models.User;
import com.ebankingproject.e_banking_backend.security.models.UserDTO;
import com.ebankingproject.e_banking_backend.security.repository.RoleRepository;
import com.ebankingproject.e_banking_backend.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapperImpl dtoMapper;
    private EmailSenderService emailSenderService;

    @Override
    public User addNewUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void addNewRole(Role role) {
         roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, ERole eRole) {
        Optional<Role> role = roleRepository.findByName(eRole);
        User user = loadUserByUsername(username);

        if (role.isPresent() && user != null) {
            user.getRoles().add(role.get());
            userRepository.save(user);
        } else if (user == null) {
            throw new RuntimeException("user null");
        }else {
            throw new RuntimeException("role doesn't exist");
        }
    }

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User Not Found with username: " + username));
    }

    @Override
    public User addNewModerator(String username, String email, String password) {
        String pw = passwordEncoder.encode(password);
        User user = addNewUser(new User(username,email,pw));
        addRoleToUser(user.getUsername(),ERole.ROLE_MODERATOR);
        return loadUserByUsername(username);
    }

    @Override
    public Page<UserDTO> findModerators(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"id"));
        Optional<Role> role = roleRepository.findByName(ERole.ROLE_MODERATOR);
        Page<User> users = userRepository.findByRolesContains(role,pageable);
        return users.map(dtoMapper::fromUser);
    }

    @Override
    public void deleteModUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public int generateCode() {
        Random random = new Random();
        int min = 10000;
        int max = 99999;
        return random.nextInt(max - min + 1) + min;
    }

    @Override
    public int sendMail(String toEmail) {
        int code = generateCode();
        emailSenderService.sendEmail(toEmail,"Verification","Votre code de vérification est : "+code);
        return code;
    }

    @Override
    public void deleteUserByCustomerId(Long customerId) {
        User user = userRepository.findByCustomerId(customerId);
        userRepository.deleteById(user.getId());
    }

    @Override
    public void updateUser(Long id, String username, String email) {
        User user = userRepository.findByCustomerId(id);
        user.setUsername(username);
        user.setEmail(email);
        userRepository.save(user);
    }

    @Override
    public UserDTO changePassword(String email,String password, String confirmedPassword) {
        if(password.equals(confirmedPassword)) {
            User user = userRepository.findByEmailContains(email);
            boolean hasAdminOrModeratorRole = user.getRoles().stream().anyMatch(role ->
                    role.getName().equals(ERole.ROLE_ADMIN) || role.getName().equals(ERole.ROLE_MODERATOR));
            if(hasAdminOrModeratorRole) {
                throw new RuntimeException("Impossible de changer le mot de passe");
            }
            user.setPassword(passwordEncoder.encode(password));
            return dtoMapper.fromUser(userRepository.save(user));
        }else{
            throw new RuntimeException("Les mots de passe sont différents");
        }
    }

    @Override
    public int checkingEmail(String address) {
        if(userRepository.existsByEmail(address)) {
            return sendMail(address);
        }else{
            throw new RuntimeException("User not found !");
        }
    }
}
