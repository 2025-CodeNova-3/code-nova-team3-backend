package com.team3.code_nova.backend.dto.auth;

import com.team3.code_nova.backend.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
public class CustomUserDetails implements UserDetails {

    @Schema(description = "유저의 아이디", example = "userId")
    private Long userId;
    @Schema(description = "유저의 이름", example = "username")
    private String username;
    @Schema(description = "유저의 비밀번호", example = "password")
    private String password;
    @Schema(description = "유저의 역할", example = "role")
    private String role;

    public CustomUserDetails(AuthUserDTO authUserDTO) {
        this.userId = authUserDTO.getUserId();
        this.username = authUserDTO.getUsername();
        this.password = "TMP_PASSWORD";
        this.role = authUserDTO.getRole();
    }

    public CustomUserDetails(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole().getValue();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                return role;
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}