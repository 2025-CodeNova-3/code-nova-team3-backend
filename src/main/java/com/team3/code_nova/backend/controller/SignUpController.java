package com.team3.code_nova.backend.controller;

import com.team3.code_nova.backend.dto.BasicApiResponse;
import com.team3.code_nova.backend.dto.EmptyResponse;
import com.team3.code_nova.backend.dto.request.SignUpRequest;
import com.team3.code_nova.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "SignUp API", description = "회원가입 API를 관리합니다.")
public class SignUpController {

    private final UserService userService;

    @Autowired
    public SignUpController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/api/auth/sign-up")
    @Operation(summary = "회원가입", description = "사용자가 회원가입을 합니다. 이미 존재하는 username 또는 email로 가입하려고 할 경우 에러를 반환합니다.")
    public ResponseEntity<?> signUp(
            @RequestBody @Parameter(description = "회원가입을 위한 요청 데이터") SignUpRequest signUpRequest){
        try {
            userService.join(signUpRequest);
            return ResponseEntity.status(200).body(
                    new BasicApiResponse<>(200, 1000, "회원가입 성공", new EmptyResponse())
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(
                    new BasicApiResponse<>(409, 1001, "중복된 username 또는 email", new EmptyResponse())
            );
        }
    }
}
