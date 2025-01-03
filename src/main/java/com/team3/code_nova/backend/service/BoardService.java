package com.team3.code_nova.backend.service;

import com.team3.code_nova.backend.dto.request.BoardRequest;
import com.team3.code_nova.backend.dto.response.BoardResponse;
import com.team3.code_nova.backend.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface BoardService {

    public BoardResponse createBoard(Long userId, BoardRequest boardRequest);

    Page<Board> getBoardsByCategory(String category, Long lastId, int page, int size);
}