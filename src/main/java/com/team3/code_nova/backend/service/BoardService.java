package com.team3.code_nova.backend.service;

import com.team3.code_nova.backend.dto.request.BoardCreateRequest;
import com.team3.code_nova.backend.dto.response.BoardCreateResponse;
import org.springframework.stereotype.Service;

@Service
public interface BoardService {

    public BoardCreateResponse createBoard(Long userId, BoardCreateRequest boardCreateRequest);
}