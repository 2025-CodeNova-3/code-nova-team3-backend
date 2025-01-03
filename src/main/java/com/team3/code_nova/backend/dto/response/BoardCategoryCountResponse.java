package com.team3.code_nova.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@AllArgsConstructor
public class BoardCategoryCountResponse {
    @Schema(description = "카테고리 별 게시글의 수", example = "\"ART\": 20")
    private Map<String, Long> categoryCounts; // 카테고리별 게시글 수
    @Schema(description = "전체 게시글 수", example = "100")
    private long totalCount; // 전체 게시글 수
}