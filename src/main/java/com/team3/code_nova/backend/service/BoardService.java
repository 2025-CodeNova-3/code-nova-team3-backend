package com.team3.code_nova.backend.service;

import com.team3.code_nova.backend.dto.request.BoardCreateRequest;
import com.team3.code_nova.backend.dto.response.BoardCategoryCountResponse;
import com.team3.code_nova.backend.dto.response.BoardCreateResponse;
import org.springframework.http.ResponseEntity;
import com.team3.code_nova.backend.dto.response.BoardListResponse;
import com.team3.code_nova.backend.enums.BoardCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface BoardService {
    BoardCreateResponse createBoard(Long userId, BoardCreateRequest boardCreateRequest);
    ResponseEntity<?> getBoardById(Long id);
    BoardCategoryCountResponse getBoardCountForCategories();
    BoardListResponse getTopBoards(BoardCategory boardCategory, Pageable pageable);
    BoardListResponse getBoards(BoardCategory boardCategory, Pageable pageable);
    ResponseEntity<?> getRecentBoards(Long userId);
    Integer getBoardCountForKeyword(String keyword);
    BoardListResponse getBoardsForKeyword(String keyword, Pageable pageable);
}