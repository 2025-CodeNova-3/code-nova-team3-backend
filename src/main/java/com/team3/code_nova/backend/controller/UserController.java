package com.team3.code_nova.backend.controller;

import com.team3.code_nova.backend.dto.BasicApiResponse;
import com.team3.code_nova.backend.dto.EmptyResponse;
import com.team3.code_nova.backend.dto.auth.CustomUserDetails;
import com.team3.code_nova.backend.entity.User;
import com.team3.code_nova.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "사용자 관련 API를 관리합니다.")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/my-info")
    @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 정보를 반환합니다. (테스트용)")
    public ResponseEntity<?> findLoginUserInfo() {
        // 아래 로직을 통해 토큰의 username 값을 Controller 로직에서 가져올 수 있음. role 값도 가능
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginUserId = customUserDetails.getUserId();

        Optional<User> userOptional = userService.findUserByUserId(loginUserId);

        if (userOptional.isPresent()) {
            return ResponseEntity.status(200).body(
                    new BasicApiResponse<>(200, 1000, "요청한 유저 정보 반환 (테스트용)", userOptional.get())
            );
        } else {
            return ResponseEntity.status(400).body(
                    new BasicApiResponse<>(400, 1000, "userId에 해당하는 유저 미존재", new EmptyResponse())
            );
        }
    }
}
