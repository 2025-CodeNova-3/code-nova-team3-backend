package com.team3.code_nova.backend.controller;

import com.team3.code_nova.backend.dto.ApiResponse;
import com.team3.code_nova.backend.dto.auth.CustomUserDetails;
import com.team3.code_nova.backend.dto.request.BoardCreateRequest;
import com.team3.code_nova.backend.dto.response.BoardCreateResponse;
import com.team3.code_nova.backend.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity createBoards(@RequestBody BoardCreateRequest boardCreateRequest) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long loginUserId = customUserDetails.getUserId();

        BoardCreateResponse response = boardService.createBoard(loginUserId, boardCreateRequest);
        return ResponseEntity.status(200).body(
                new ApiResponse<>(200, 0,"게시글 생성 완료", response)
        );
    }
}