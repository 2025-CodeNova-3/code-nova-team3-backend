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

    @GetMapping("/paged")
    public ResponseEntity getBoards(@RequestParam(name = "boardCategory", required = false) String boardCategoryStr,
                                    @RequestParam(name = "page") int page,
                                    @RequestParam(name = "size") int size) {

        // "ALL" 문자열을 BoardCategory의 null 값으로 처리
        BoardCategory boardCategory = null;
        if (boardCategoryStr != null && !boardCategoryStr.equals("ALL")) {
            try {
                boardCategory = BoardCategory.valueOf(boardCategoryStr); // BoardCategory Enum으로 변환
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(400).body(
                        new ApiResponse<>(400, -1, "유효하지 않은 카테고리입니다.", null)
                );
            }
        }

        Pageable pageable = PageRequest.of(page, size);
        BoardListResponse boardListResponse = boardService.getBoards(boardCategory, pageable);

        return ResponseEntity.status(200).body(
                new ApiResponse<>(200, 0, "게시글 목록 반환", boardListResponse)
        );
    }


    @GetMapping("/recent")
    public ResponseEntity<?> getRecentBoards(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            return boardService.getRecentBoards(userDetails.getUserId());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResponse<>(500, -1, "서버 오류: " + e.getMessage(), null)
            );
        }
    }

    @GetMapping("/keyword/counts")
    public ResponseEntity getBoardsNumContainsKeyword(@RequestParam(name = "keyword") String keyword) {
        Integer resultCount = boardService.getBoardCountForKeyword(keyword);

        return ResponseEntity.status(200).body(
                new ApiResponse<>(200, 0, "검색어 포함 게시글 개수 반환", resultCount)
        );
    }

    @GetMapping("/keyword/paged")
    public ResponseEntity getBoardsContainsKeywordBeforeLastId(@RequestParam(name = "lastId") Long lastId,
                                                               @RequestParam(name = "keyword") String keyword,
                                                               @RequestParam(name = "page") int page,
                                                               @RequestParam(name = "size") int size) {

        Pageable pageable = PageRequest.of(page, size);
        BoardListResponse boardListResponse = boardService.getBoardsForKeywordBeforeLastId(lastId, keyword, pageable);

        return ResponseEntity.status(200).body(
                new ApiResponse<>(200, 0, "검색어 포함 게시글 목록 반환", boardListResponse)
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
}