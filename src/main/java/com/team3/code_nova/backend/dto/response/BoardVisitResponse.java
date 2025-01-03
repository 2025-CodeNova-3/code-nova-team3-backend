package com.team3.code_nova.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BoardVisitResponse {
    private Long boardId;
    private String title;
    private String boardCategory;
    private String openContent;
    private String hiddenContent;
    private Long views;
    private String openTime;
    private String authorName;  // 작성자 이름
    private LocalDateTime createdAt;  // 게시글 생성 시간
}
