package com.team3.code_nova.backend.dto;

import com.team3.code_nova.backend.entity.Board;
import com.team3.code_nova.backend.enums.BoardCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardListDTO {

    @Schema(description = "게시글 ID", example = "107")
    private Long boardId; // 게시글 ID

    @Schema(description = "게시글 카테고리", example = "BRAND")
    private BoardCategory boardCategory; // 게시글 카테고리

    @Schema(description = "게시글 제목", example = "Sample Title")
    private String title; // 게시글 제목

    @Schema(description = "공개 내용", example = "This is open content.")
    private String openContent; // 공개 내용

    @Schema(description = "비공개 내용", example = "This is hidden content.")
    private String hiddenContent; // 비공개 내용

    @Schema(description = "공개 기간(일)", example = "30")
    private Integer openDuration; // 공개 기간

    @Schema(description = "조회수", example = "120")
    private Long views; // 조회수

    @Schema(description = "작성자 사용자명", example = "user1234")
    private String username; // 작성자 사용자명

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
