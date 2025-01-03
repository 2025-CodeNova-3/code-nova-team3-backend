package com.team3.code_nova.backend.dto.response;

import com.team3.code_nova.backend.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponse {
    private Long commentId;
    private String content;
    private String username;
    private Boolean beforeOpen;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .username(comment.getUser().getUsername())
                .beforeOpen(comment.getBeforeOpen())
                .build();
    }
}