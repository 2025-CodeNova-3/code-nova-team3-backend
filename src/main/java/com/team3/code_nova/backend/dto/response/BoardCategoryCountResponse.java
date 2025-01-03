package com.team3.code_nova.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class BoardCategoryCountResponse {
    private Map<String, Long> categoryCounts; // 카테고리별 게시글 수
    private long totalCount; // 전체 게시글 수
}