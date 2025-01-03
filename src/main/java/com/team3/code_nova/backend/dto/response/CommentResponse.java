package com.team3.code_nova.backend.dto.response;

import com.team3.code_nova.backend.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Builder
public class CommentResponse {

    @Schema(description = "댓글 ID", example = "301")
    private Long commentId;

    @Schema(description = "댓글 내용", example = "This is a sample comment.")
    private String content;

    @Schema(description = "작성자 사용자명", example = "commenter123")
    private String username;

    @Schema(description = "공개 전 여부", example = "true")
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
