package com.team3.code_nova.backend.service;

import com.team3.code_nova.backend.dto.request.CommentCreateRequest;
import org.springframework.http.ResponseEntity;

public interface CommentService {
    ResponseEntity<?> createComment(Long userId, Long boardId, CommentCreateRequest request);
    ResponseEntity<?> getComments(Long userId, Long boardId, Boolean beforeOpen);
    ResponseEntity<?> deleteComment(Long userId, Long commentId);
}