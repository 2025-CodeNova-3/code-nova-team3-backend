package com.team3.code_nova.backend.controller;

import com.team3.code_nova.backend.dto.ApiResponse;
import com.team3.code_nova.backend.dto.EmptyResponse;
import com.team3.code_nova.backend.dto.auth.CustomUserDetails;
import com.team3.code_nova.backend.entity.User;
import com.team3.code_nova.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/my-info")
    public ResponseEntity findLoginUserInfo() {
        // 아래 로직을 통해 토큰의 username 값을 Controller 로직에서 가져올 수 있음. role 값도 가능
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginUserId = customUserDetails.getUserId();

        Optional<User> userOptional = userService.findUserByUserId(loginUserId);

        if (userOptional.isPresent()) {
            return ResponseEntity.status(200).body(
                    new ApiResponse<>(200, 1000,"유저 정보 반환 (테스트용)", userOptional.get())
            );
        } else {
            return ResponseEntity.status(400).body(
                    new ApiResponse<>(400, 1000,"userId에 해당하는 유저 미존재", new EmptyResponse())
            );
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> findUserById(@PathVariable("userId") Long userId) {
        // 아래 로직을 통해 토큰의 username 값을 Controller 로직에서 가져올 수 있음. role 값도 가능
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(customUserDetails.getUserId()+" requested "+userId+"'s INFO");

        Optional<User> userOptional = userService.findUserByUserId(userId);

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/username")
    public ResponseEntity<User> findUserById(@RequestParam("username") String username) {
        Optional<User> userOptional = userService.findUserByUsername(username);

        if (userOptional.isPresent()) return ResponseEntity.ok(userOptional.get());
        else                          return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
