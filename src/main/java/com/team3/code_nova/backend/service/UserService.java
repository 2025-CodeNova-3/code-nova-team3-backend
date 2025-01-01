package com.team3.code_nova.backend.service;

import com.team3.code_nova.backend.dto.SignUpDTO;
import com.team3.code_nova.backend.entity.User;

import java.util.Optional;

public interface UserService {

    public void join(SignUpDTO signUpDTO);

    public Optional<User> findUserByUserId(Long userId);

    public Optional<User> findUserByUsername(String username);
}
