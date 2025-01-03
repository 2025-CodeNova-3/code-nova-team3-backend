package com.team3.code_nova.backend.dto.response;

import com.team3.code_nova.backend.dto.BoardListDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BoardListResponse {

    @Schema(description = "게시글 목록", example = "[\n" +
            "  {\n" +
            "    \"boardId\": 107,\n" +
            "    \"boardCategory\": \"BRAND\",\n" +
            "    \"title\": \"Sample Title\",\n" +
            "    \"openContent\": \"This is open content.\",\n" +
            "    \"hiddenContent\": \"This is hidden content.\",\n" +
            "    \"openDuration\": 30,\n" +
            "    \"views\": 120,\n" +
            "    \"username\": \"user1234\"\n" +
            "  }\n" +
            "]")
    private List<BoardListDTO> boards; // 게시글 목록

    @Schema(description = "전체 게시글 수", example = "100")
    private long totalElements; // 전체 게시글 수

    @Schema(description = "전체 페이지 수", example = "10")
    private int totalPages; // 전체 페이지 수
}
