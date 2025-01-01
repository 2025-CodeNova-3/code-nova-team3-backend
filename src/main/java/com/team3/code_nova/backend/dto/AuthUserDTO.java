package com.team3.code_nova.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthUserDTO {

    private Long userId;

    private String role;

    private String username;
}