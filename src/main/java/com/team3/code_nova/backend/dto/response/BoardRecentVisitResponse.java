package com.team3.code_nova.backend.dto.response;

import com.team3.code_nova.backend.entity.Board;
import com.team3.code_nova.backend.enums.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

// BoardVisitDTO.java
@Getter
@AllArgsConstructor
public class BoardRecentVisitResponse {
    private Long boardId;
    private String title;
    private BoardCategory boardCategory;
    private Long views;
    private String authorName;
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