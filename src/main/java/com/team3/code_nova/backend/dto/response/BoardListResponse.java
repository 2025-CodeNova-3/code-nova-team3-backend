package com.team3.code_nova.backend.dto.response;

import com.team3.code_nova.backend.dto.BoardListDTO;
import com.team3.code_nova.backend.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BoardListResponse {
    private List<BoardListDTO> boards;  // 게시글 목록
    private long totalElements;   // 전체 게시글 수
    private int totalPages;       // 전체 페이지 수
}
