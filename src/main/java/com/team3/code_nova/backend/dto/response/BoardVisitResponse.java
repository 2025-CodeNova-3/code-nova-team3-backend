package com.team3.code_nova.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BoardVisitResponse {

    @Schema(description = "게시글 ID", example = "102")
    private Long boardId;

    @Schema(description = "게시글 제목", example = "Visit Post Title")
    private String title;

    @Schema(description = "게시글 카테고리", example = "EVENT")
    private String boardCategory;

    @Schema(description = "공개 내용", example = "This is the open content of the post.")
    private String openContent;

    @Schema(description = "비공개 내용", example = "This is the hidden content of the post.")
    private String hiddenContent;

    @Schema(description = "조회수", example = "512")
    private Long views;

    @Schema(description = "공개 시간", example = "2023-12-01T12:00:00")
    private String openTime;

    @Schema(description = "작성자 이름", example = "user456")
    private String authorName;

    @Schema(description = "게시글 생성 시간", example = "2023-12-01T08:00:00")
    private LocalDateTime createdAt;
}
