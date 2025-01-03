package com.team3.code_nova.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardResponse {
    private Long boardId;
    private String title;
    private String boardCategory;
    private Integer openDuration;
    private Long views;
}