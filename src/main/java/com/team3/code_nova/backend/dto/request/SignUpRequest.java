package com.team3.code_nova.backend.dto.request;

import com.team3.code_nova.backend.entity.User;
import lombok.Getter;
import static com.team3.code_nova.backend.enums.Role.USER;
import static com.team3.code_nova.backend.enums.Status.ACTIVE;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
public class SignUpRequest {
    @Schema(description = "username", example = "username")
    private String username;
    @Schema(description = "password", example = "password")
    private String password;
    @Schema(description = "email", example = "email")
    private String email;

    public User toActiveUser(){
        return User.builder()
                .username(username).password(password).email(email)
                .role(USER).status(ACTIVE).build();
    }
}
