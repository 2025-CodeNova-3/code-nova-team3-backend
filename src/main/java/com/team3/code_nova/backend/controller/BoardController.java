package com.team3.code_nova.backend.controller;

import com.team3.code_nova.backend.dto.BasicApiResponse;
import com.team3.code_nova.backend.dto.auth.CustomUserDetails;
import com.team3.code_nova.backend.dto.request.BoardCreateRequest;
import com.team3.code_nova.backend.dto.response.BoardCategoryCountResponse;
import com.team3.code_nova.backend.dto.response.BoardCreateResponse;
import com.team3.code_nova.backend.dto.response.BoardListResponse;
import com.team3.code_nova.backend.enums.BoardCategory;
import com.team3.code_nova.backend.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@Tag(name = "Board API", description = "게시판 관련 API를 관리합니다.")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    @Operation(summary = "게시글 생성", description = "사용자가 게시글을 생성합니다.")
    public ResponseEntity<?> createBoards(
            @RequestBody BoardCreateRequest boardCreateRequest) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long loginUserId = customUserDetails.getUserId();

        BoardCreateResponse response = boardService.createBoard(loginUserId, boardCreateRequest);
        return ResponseEntity.status(200).body(
                new BasicApiResponse<>(200, 0, "게시글 생성 완료", response)
        );
    }


    @GetMapping("/{boardId}")
    @Operation(summary = "게시글 상세 조회", description = "특정 ID의 게시글을 조회합니다.")
    public ResponseEntity<?> getBoardById(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            return boardService.getBoardById(boardId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new BasicApiResponse<>(500, -1, "서버 오류: " + e.getMessage(), null)
            );
        }
    }

    @GetMapping("/counts")
    @Operation(summary = "카테고리별 게시글 개수", description = "모든 카테고리별 게시글의 개수를 반환합니다.")
    public ResponseEntity<?> getBoardsNumForCategories() {
        BoardCategoryCountResponse response = boardService.getBoardCountForCategories();
        return ResponseEntity.status(200).body(
                new BasicApiResponse<>(200, 0, "카테고리별 게시글 개수 반환", response)
        );
    }

    @GetMapping("/paged")
    @Operation(summary = "게시글 목록 조회", description = "특정 마지막 ID 이전의 게시글 목록을 페이지네이션 형태로 반환합니다."  
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
    @Operation(summary = "최근 게시글 조회", description = "사용자가 최근에 조회한 게시글 목록을 반환합니다.")
    public ResponseEntity<?> getRecentBoards(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            return boardService.getRecentBoards(userDetails.getUserId());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new BasicApiResponse<>(500, -1, "서버 오류: " + e.getMessage(), null)
            );
        }
    }

    @GetMapping("/keyword/counts")
    @Operation(summary = "검색어 포함 게시글 개수", description = "특정 검색어를 포함하는 게시글의 개수를 반환합니다.")
    public ResponseEntity<?> getBoardsNumContainsKeyword(@RequestParam(name = "keyword") String keyword) {
        Integer resultCount = boardService.getBoardCountForKeyword(keyword);

        return ResponseEntity.status(200).body(
                new BasicApiResponse<>(200, 0, "검색어 포함 게시글 개수 반환", resultCount)
        );
    }

    @GetMapping("/keyword/paged")
    @Operation(summary = "검색어 포함 게시글 목록 조회", description = "특정 검색어를 포함한 게시글을 페이지네이션 형태로 반환합니다.")
    public ResponseEntity getBoardsContainsKeyword(@RequestParam(name = "keyword") String keyword,
                                                   @RequestParam(name = "page") int page,
                                                   @RequestParam(name = "size") int size) {

        Pageable pageable = PageRequest.of(page, size);
        BoardListResponse boardListResponse = boardService.getBoardsForKeyword(keyword, pageable);

        return ResponseEntity.status(200).body(
                new BasicApiResponse<>(200, 0, "검색어 포함 게시글 목록 반환", boardListResponse)
        );
    }

    @GetMapping("/top")
    @Operation(summary = "상위 게시글 조회", description = "조회수가 높은 상위 게시글 목록을 반환합니다.")
    public ResponseEntity<?> getTopBoards(
            @RequestParam(name = "boardCategory", required = false) BoardCategory boardCategory,
            @RequestParam(name = "size") int size) {

        Pageable pageable = PageRequest.of(0, size);
        BoardListResponse boardListResponse = boardService.getTopBoards(boardCategory, pageable);

        return ResponseEntity.status(200).body(
                new BasicApiResponse<>(200, 0, "게시글 상위 목록 반환", boardListResponse)
        );
    }
}
