package com.team3.code_nova.backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
public class CommentCreateRequest {
    @Schema(description = "댓글 내용", example = "댓글 내용")
    private String content;
    @Schema(description = "댓글을 쓴 시점이 open을 보기 이전이면 true", example = "true")
    private Boolean beforeOpen;
}