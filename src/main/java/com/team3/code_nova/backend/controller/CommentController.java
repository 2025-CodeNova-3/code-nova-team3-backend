package com.team3.code_nova.backend.controller;

import com.team3.code_nova.backend.dto.ApiResponse;
import com.team3.code_nova.backend.dto.auth.CustomUserDetails;
import com.team3.code_nova.backend.dto.request.CommentCreateRequest;
import com.team3.code_nova.backend.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/boards/{boardId}")
    public ResponseEntity<?> createComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long boardId,
            @RequestBody CommentCreateRequest request) {
        try {
            return commentService.createComment(userDetails.getUserId(), boardId, request);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResponse<>(500, -1, "서버 오류: " + e.getMessage(), null)
            );
        }
    }

    @GetMapping("/boards/{boardId}")
    public ResponseEntity<?> getComments(
            @PathVariable Long boardId,
            @RequestParam(required = false) Boolean beforeOpen) {
        try {
            return commentService.getComments(boardId, beforeOpen);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResponse<>(500, -1, "서버 오류: " + e.getMessage(), null)
            );
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long commentId) {
        try {
            return commentService.deleteComment(userDetails.getUserId(), commentId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResponse<>(500, -1, "서버 오류: " + e.getMessage(), null)
            );
        }
    }
}