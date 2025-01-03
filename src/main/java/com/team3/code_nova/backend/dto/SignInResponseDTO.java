package com.team3.code_nova.backend.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SignInResponseDTO {
    private final String access;

    private final String refresh;

    private final Long userId;
}
