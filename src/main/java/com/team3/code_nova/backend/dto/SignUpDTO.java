package com.team3.code_nova.backend.dto;

import com.team3.code_nova.backend.entity.User;
import lombok.Getter;

import static com.team3.code_nova.backend.enums.Role.USER;
import static com.team3.code_nova.backend.enums.Status.ACTIVE;

@Getter
public class SignUpDTO {
    private String username;

    private String password;

    private String email;

    public User toActiveUser(){
        return User.builder()
                .username(username).password(password).email(email)
                .role(USER).status(ACTIVE).build();
    }
}
