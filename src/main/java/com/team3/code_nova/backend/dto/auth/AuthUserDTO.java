package com.team3.code_nova.backend.dto.auth;

import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter @Setter
public class AuthUserDTO {

    @Schema(description = "유저 아이디", example = "userId")
    private Long userId;

    @Schema(description = "유저의 역할", example = "role")
    private String role;

    @Schema(description = "유저 이름", example = "username")
    private String username;
}