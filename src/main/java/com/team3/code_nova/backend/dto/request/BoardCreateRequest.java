package com.team3.code_nova.backend.dto.request;

import com.team3.code_nova.backend.enums.BoardCategory;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
public class BoardCreateRequest {

    @Schema(description = "게시글 카테고리", example = "BRAND")
    private BoardCategory boardCategory;
    @Schema(description = "게시글 제목", example = "Title")
    private String title;
    @Schema(description = "항상 볼 수 있는 내용", example = "항상 볼 수 있는 내용")
    private String openContent;
    @Schema(description = "글쓴이가 정한 시간 이후에 볼 수 있는 내용", example = "글쓴이가 정한 시간 이후에 볼 수 있는 내용")
    private String hiddenContent;
    @Schema(description = "분 단위 시간", example = "20")
    private Integer openDuration;
}