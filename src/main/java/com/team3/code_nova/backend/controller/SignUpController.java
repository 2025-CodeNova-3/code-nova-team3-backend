package com.team3.code_nova.backend.controller;

import com.team3.code_nova.backend.dto.ApiResponse;
import com.team3.code_nova.backend.dto.EmptyResponse;
import com.team3.code_nova.backend.dto.request.SignUpRequest;
import com.team3.code_nova.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest){
        try {
            userService.join(signUpRequest);
            return ResponseEntity.status(200).body(
                    new ApiResponse<>(200, 1000,"회원가입 성공", new EmptyResponse())
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(
                    new ApiResponse<>(409, 1001,"중복된 username 또는 email", new EmptyResponse())
            );
        }
    }
}