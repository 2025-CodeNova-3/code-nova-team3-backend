package com.team3.code_nova.backend.controller;

import com.team3.code_nova.backend.dto.ApiResponse;
import com.team3.code_nova.backend.dto.EmptyResponse;
import com.team3.code_nova.backend.dto.auth.CustomUserDetails;
import com.team3.code_nova.backend.dto.request.BoardCreateRequest;
import com.team3.code_nova.backend.dto.response.BoardCategoryCountResponse;
import com.team3.code_nova.backend.dto.response.BoardCreateResponse;
import com.team3.code_nova.backend.dto.response.BoardListResponse;
import com.team3.code_nova.backend.enums.BoardCategory;
import com.team3.code_nova.backend.service.BoardService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<?> createBoards(@RequestBody BoardCreateRequest boardCreateRequest) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long loginUserId = customUserDetails.getUserId();

        BoardCreateResponse response = boardService.createBoard(loginUserId, boardCreateRequest);
        return ResponseEntity.status(200).body(
                new ApiResponse<>(200, 0,"게시글 생성 완료", response)
        );
    }


    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoardById(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            return boardService.getBoardById(boardId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResponse<>(500, -1, "서버 오류: " + e.getMessage(), null)
            );
        }
    }

    @GetMapping("/counts")
    public ResponseEntity getBoardsNumForCategories() {
        BoardCategoryCountResponse response = boardService.getBoardCountForCategories();
        return ResponseEntity.status(200).body(
                new ApiResponse<>(200, 0, "카테고리별 게시글 개수 반환", response)
        );
    }

    @GetMapping("/top")
    public ResponseEntity getTopBoards(@RequestParam(name = "boardCategory", required = false) BoardCategory boardCategory,
                                       @RequestParam(name = "size") int size) {

        Pageable pageable = PageRequest.of(0, size);
        BoardListResponse boardListResponse = boardService.getTopBoards(boardCategory, pageable);

        return ResponseEntity.status(200).body(
                new ApiResponse<>(200, 0,"게시글 상위 목록 반환", boardListResponse)
        );
    }

    @GetMapping("/paged")
    public ResponseEntity getBoardsBeforeLastId(@RequestParam(name = "lastId") Long lastId,
                                                @RequestParam(name = "boardCategory", required = false) BoardCategory boardCategory,
                                                @RequestParam(name = "page") int page,
                                                @RequestParam(name = "size") int size) {

        Pageable pageable = PageRequest.of(page, size);
        BoardListResponse boardListResponse = boardService.getBoardsBeforeLastId(lastId, boardCategory, pageable);
        
        return ResponseEntity.status(200).body(
                new ApiResponse<>(200, 0,"게시글 목록 반환", boardListResponse)
        );
    }
}