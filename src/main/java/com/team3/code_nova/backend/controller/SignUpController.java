package com.team3.code_nova.backend.controller;

import com.team3.code_nova.backend.dto.ApiResponse;
import com.team3.code_nova.backend.dto.EmptyResponse;
import com.team3.code_nova.backend.dto.SignUpDTO;
import com.team3.code_nova.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignUpController {

    private final UserService userService;

    @Autowired
    public SignUpController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/api/auth/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpDTO signUpDTO){
        try {
            userService.join(signUpDTO);
            return ResponseEntity.status(200).body(
                    new ApiResponse<>(200, 1000,"회원가입 성공", new EmptyResponse())
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(
                    new ApiResponse<>(409, 1001,"이미 존재하는 username", new EmptyResponse())
            );
        }
    }
}