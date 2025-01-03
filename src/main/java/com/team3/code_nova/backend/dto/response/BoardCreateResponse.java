package com.team3.code_nova.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Builder
public class BoardCreateResponse {
    @Schema(description = "보드의 아이디", example = "1")
    private Long boardId;
    @Schema(description = "게시글의 제목", example = "title")
    private String title;
    @Schema(description = "게시글의 카테고리", example = "ART")
    private String boardCategory;
    @Schema(description = "게시글의 숨겨진 내용을 볼 수 있는 시간", example = "20")
    private Integer openDuration;
    @Schema(description = "게시글의 조회수", example = "0")
    private Long views;
}