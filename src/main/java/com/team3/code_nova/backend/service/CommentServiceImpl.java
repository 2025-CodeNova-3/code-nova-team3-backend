package com.team3.code_nova.backend.service;

import com.team3.code_nova.backend.dto.ApiResponse;
import com.team3.code_nova.backend.dto.request.CommentCreateRequest;
import com.team3.code_nova.backend.dto.response.CommentResponse;
import com.team3.code_nova.backend.entity.Board;
import com.team3.code_nova.backend.entity.Comment;
import com.team3.code_nova.backend.entity.User;
import com.team3.code_nova.backend.repository.BoardRepository;
import com.team3.code_nova.backend.repository.CommentRepository;
import com.team3.code_nova.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              UserRepository userRepository,
                              BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<?> createComment(Long userId, Long boardId, CommentCreateRequest request) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

            Comment comment = new Comment();
            comment.setContent(request.getContent());
            comment.setBeforeOpen(request.getBeforeOpen());
            comment.setUser(user);
            comment.setBoard(board);

            Comment savedComment = commentRepository.save(comment);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, 0, "댓글이 생성되었습니다.", CommentResponse.from(savedComment))
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(400, -1, e.getMessage(), null)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResponse<>(500, -1, "서버 오류가 발생했습니다.", null)
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> getComments(Long boardId, Boolean beforeOpen) {
        try {
            List<Comment> comments;
            if (beforeOpen != null) {
                comments = commentRepository.findByBoard_BoardIdAndBeforeOpenOrderByCreatedAtDesc(boardId, beforeOpen);
            } else {
                comments = commentRepository.findByBoard_BoardIdOrderByCreatedAtDesc(boardId);
            }

            List<CommentResponse> commentResponses = comments.stream()
                    .map(CommentResponse::from)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(
                    new ApiResponse<>(200, 0, "댓글 목록을 조회했습니다.", commentResponses)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResponse<>(500, -1, "서버 오류가 발생했습니다.", null)
            );
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteComment(Long userId, Long commentId) {
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

            if (!comment.getUser().getUserId().equals(userId)) {
                return ResponseEntity.badRequest().body(
                        new ApiResponse<>(400, -1, "댓글 삭제 권한이 없습니다.", null)
                );
            }

            commentRepository.delete(comment);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, 0, "댓글이 삭제되었습니다.", null)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(400, -1, e.getMessage(), null)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResponse<>(500, -1, "서버 오류가 발생했습니다.", null)
            );
        }
    }
}