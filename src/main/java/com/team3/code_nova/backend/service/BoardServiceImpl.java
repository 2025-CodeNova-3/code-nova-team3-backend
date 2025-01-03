package com.team3.code_nova.backend.service;

import com.team3.code_nova.backend.dto.request.BoardRequest;
import com.team3.code_nova.backend.dto.response.BoardResponse;
import com.team3.code_nova.backend.entity.Board;
import com.team3.code_nova.backend.entity.User;
import com.team3.code_nova.backend.repository.BoardRepository;
import com.team3.code_nova.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public BoardResponse createBoard(Long userId, BoardRequest boardRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Board board = new Board();
        board.setBoardCategory(boardRequest.getBoardCategory());
        board.setTitle(boardRequest.getTitle());
        board.setOpenContent(boardRequest.getOpenContent());
        board.setHiddenContent(boardRequest.getHiddenContent());
        board.setOpenDuration(boardRequest.getOpenDuration());
        board.setViews(0L);
        board.setUser(user);

        Board savedBoard = boardRepository.save(board);

        return BoardResponse.builder()
                .boardId(savedBoard.getBoardId())
                .title(savedBoard.getTitle())
                .boardCategory(savedBoard.getBoardCategory().name())
                .openDuration(savedBoard.getOpenDuration())
                .views(savedBoard.getViews())
                .build();
    }


    public Page<Board> getBoardsByCategory(String category, Long lastId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        // category가 "all"이면 전체 게시글을 가져오고, 아니면 해당 category의 게시글을 가져옴
        if ("all".equalsIgnoreCase(category)) {
            return boardRepository.findByIdLessThan(lastId, pageable);
        } else {
            return boardRepository.findByCategoryAndIdLessThan(category, lastId, pageable);
        }
    }
}
