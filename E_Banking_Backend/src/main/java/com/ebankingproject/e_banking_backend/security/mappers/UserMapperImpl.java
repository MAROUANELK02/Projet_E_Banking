package com.ebankingproject.e_banking_backend.security.mappers;

import com.ebankingproject.e_banking_backend.security.models.User;
import com.ebankingproject.e_banking_backend.security.models.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserMapperImpl {
    public UserDTO fromUser(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user,userDTO);
        return userDTO;
    }
}
