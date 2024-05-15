package com.ebankingproject.e_banking_backend.web;

import com.ebankingproject.e_banking_backend.security.models.User;
import com.ebankingproject.e_banking_backend.security.models.UserDTO;
import com.ebankingproject.e_banking_backend.security.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/moderator")
public class AdminRestControllers {
    private AccountService accountService;

    @PostMapping("/addModerator")
    @PreAuthorize("hasRole('ADMIN')")
    public User saveModerator(@RequestBody UserDTO userDTO) {
         return accountService.addNewModerator(userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserDTO> getModerators(@RequestParam(name = "page",defaultValue = "0") int page,
                                       @RequestParam(name = "size",defaultValue = "6") int size) {
        return accountService.findModerators(page,size);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteModerator(@PathVariable(name = "id")Long userId) {
        accountService.deleteModUser(userId);
    }
}
