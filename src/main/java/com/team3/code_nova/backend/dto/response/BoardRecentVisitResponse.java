package com.team3.code_nova.backend.dto.response;

import com.team3.code_nova.backend.entity.Board;
import com.team3.code_nova.backend.enums.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BoardRecentVisitResponse {

    @Schema(description = "게시글 ID", example = "101")
    private Long boardId;

    @Schema(description = "게시글 제목", example = "Sample Title")
    private String title;

    @Schema(description = "게시글 카테고리", example = "BRAND")
    private BoardCategory boardCategory;

    @Schema(description = "조회수", example = "256")
    private Long views;

    @Schema(description = "작성자 이름", example = "author123")
    private String authorName;

    @Schema(description = "공개 시작 시간", example = "2023-12-01T10:00:00")
    private LocalDateTime openTime;

    public BoardRecentVisitResponse(Board board, LocalDateTime openTime) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.boardCategory = board.getBoardCategory();
        this.views = board.getViews();
        this.authorName = board.getUser().getUsername();
        this.openTime = openTime;
    }
}
