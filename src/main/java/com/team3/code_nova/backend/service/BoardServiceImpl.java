package com.team3.code_nova.backend.service;

import com.team3.code_nova.backend.dto.request.BoardCreateRequest;
import com.team3.code_nova.backend.dto.response.BoardCreateResponse;
import com.team3.code_nova.backend.entity.Board;
import com.team3.code_nova.backend.entity.User;
import com.team3.code_nova.backend.repository.BoardRepository;
import com.team3.code_nova.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BoardCreateResponse createBoard(Long userId, BoardCreateRequest boardCreateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Board board = new Board();
        board.setBoardCategory(boardCreateRequest.getBoardCategory());
        board.setTitle(boardCreateRequest.getTitle());
        board.setOpenContent(boardCreateRequest.getOpenContent());
        board.setHiddenContent(boardCreateRequest.getHiddenContent());
        board.setOpenDuration(boardCreateRequest.getOpenDuration());
        board.setViews(0L);
        board.setUser(user);

        Board savedBoard = boardRepository.save(board);

        return BoardCreateResponse.builder()
                .boardId(savedBoard.getBoardId())
                .title(savedBoard.getTitle())
                .boardCategory(savedBoard.getBoardCategory().name())
                .openDuration(savedBoard.getOpenDuration())
                .views(savedBoard.getViews())
                .build();
    }
}
