package com.team3.code_nova.backend.repository;

import com.team3.code_nova.backend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoard_BoardIdOrderByCreatedAtDesc(Long boardId);
    List<Comment> findByBoard_BoardIdAndBeforeOpenOrderByCreatedAtDesc(Long boardId, Boolean beforeOpen);
}