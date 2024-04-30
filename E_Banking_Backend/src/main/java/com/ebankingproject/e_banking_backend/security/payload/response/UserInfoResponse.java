package com.ebankingproject.e_banking_backend.security.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    private String token;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}
