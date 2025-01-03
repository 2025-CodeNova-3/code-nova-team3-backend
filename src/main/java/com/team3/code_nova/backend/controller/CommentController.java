package com.team3.code_nova.backend.controller;

import com.team3.code_nova.backend.dto.BasicApiResponse;
import com.team3.code_nova.backend.dto.auth.CustomUserDetails;
import com.team3.code_nova.backend.dto.request.CommentCreateRequest;
import com.team3.code_nova.backend.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comment API", description = "댓글 관련 API를 관리합니다.")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/boards/{boardId}")
    @Operation(summary = "댓글 생성", description = "게시글에 댓글을 생성합니다.")
    public ResponseEntity<?> createComment(
            @AuthenticationPrincipal @Parameter(description = "현재 로그인한 사용자 정보") CustomUserDetails userDetails,
            @PathVariable @Parameter(description = "댓글을 달 게시글 ID") Long boardId,
            @RequestBody @Parameter(description = "댓글 생성 요청 객체") CommentCreateRequest request) {
        try {
            return commentService.createComment(userDetails.getUserId(), boardId, request);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new BasicApiResponse<>(500, -1, "서버 오류: " + e.getMessage(), null)
            );
        }
    }

    @GetMapping("/boards/{boardId}")
    @Operation(summary = "댓글 조회", description = "특정 게시글에 대한 댓글 목록을 조회합니다.")
    public ResponseEntity<?> getComments(
            @PathVariable @Parameter(description = "댓글을 조회할 게시글 ID") Long boardId,
            @RequestParam(required = false) @Parameter(description = "댓글을 비공개로 조회할지 여부") Boolean beforeOpen) {
        try {
            return commentService.getComments(boardId, beforeOpen);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new BasicApiResponse<>(500, -1, "서버 오류: " + e.getMessage(), null)
            );
        }
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "특정 댓글을 삭제합니다.")
    public ResponseEntity<?> deleteComment(
            @AuthenticationPrincipal @Parameter(description = "현재 로그인한 사용자 정보") CustomUserDetails userDetails,
            @PathVariable @Parameter(description = "삭제할 댓글 ID") Long commentId) {
        try {
            return commentService.deleteComment(userDetails.getUserId(), commentId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new BasicApiResponse<>(500, -1, "서버 오류: " + e.getMessage(), null)
            );
        }
    }
}
