package com.team3.code_nova.backend.dto;

import com.team3.code_nova.backend.entity.Board;
import com.team3.code_nova.backend.enums.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardListDTO {
    private Long boardId;
    private BoardCategory boardCategory;
    private String title;
    private String openContent;
    private String hiddenContent;
    private Integer openDuration;
    private Long views;
    private String username;  // user의 username만 포함

    public BoardListDTO(Board board) {
        this.boardId = board.getBoardId();
        this.boardCategory = board.getBoardCategory();
        this.title = board.getTitle();
        this.openContent = board.getOpenContent();
        this.hiddenContent = board.getHiddenContent();
        this.openDuration = board.getOpenDuration();
        this.views = board.getViews();
        this.username = board.getUser().getUsername();
    }
}
