package com.team3.code_nova.backend.controller;

import com.team3.code_nova.backend.dto.ApiResponse;
import com.team3.code_nova.backend.dto.EmptyResponse;
import com.team3.code_nova.backend.dto.auth.CustomUserDetails;
import com.team3.code_nova.backend.dto.request.BoardRequest;
import com.team3.code_nova.backend.dto.response.BoardResponse;
import com.team3.code_nova.backend.entity.Board;
import com.team3.code_nova.backend.service.BoardService;
import org.springframework.data.domain.Page;
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
    public ResponseEntity createBoards(@RequestBody BoardRequest boardRequest) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long loginUserId = customUserDetails.getUserId();

        BoardResponse response = boardService.createBoard(loginUserId, boardRequest);
        return ResponseEntity.status(200).body(
                new ApiResponse<>(200, 0,"게시글 생성 완료", response)
        );
    }

    @GetMapping
    public ResponseEntity getBoardsByCategory(
            @RequestParam("category") String category,
            @RequestParam("lastId") Long lastId,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Page<Board> boards = boardService.getBoardsByCategory(category, lastId, page, size);
        return ResponseEntity.status(200).body(
                new ApiResponse<>(200, 0,"게시글 페이징 반환", boards)
        );
    }
}