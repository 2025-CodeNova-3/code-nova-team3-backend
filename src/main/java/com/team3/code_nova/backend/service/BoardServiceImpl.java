package com.team3.code_nova.backend.service;

import com.team3.code_nova.backend.dto.BoardListDTO;
import com.team3.code_nova.backend.dto.request.BoardCreateRequest;
import com.team3.code_nova.backend.dto.response.BoardCategoryCountResponse;
import com.team3.code_nova.backend.dto.response.BoardCreateResponse;
import com.team3.code_nova.backend.dto.response.BoardListResponse;
import com.team3.code_nova.backend.entity.Board;
import com.team3.code_nova.backend.entity.User;
import com.team3.code_nova.backend.enums.BoardCategory;
import com.team3.code_nova.backend.repository.BoardRepository;
import com.team3.code_nova.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public BoardCategoryCountResponse getBoardCountForCategories() {
        // 카테고리별 게시글 수를 저장할 Map
        Map<String, Long> categoryCounts = new HashMap<>();

        // 모든 카테고리 순회하며 게시글 수 구하기
        for (BoardCategory category : BoardCategory.values()) {
            long count = boardRepository.countByBoardCategory(category);
            categoryCounts.put(category.name(), count);
        }

        // 전체 게시글 수 구하기
        long totalCount = boardRepository.count();

        // 응답 객체 생성하여 반환
        return new BoardCategoryCountResponse(categoryCounts, totalCount);
    }

    @Override
    public BoardListResponse getBoardsBeforeLastId(Long lastId, BoardCategory boardCategory, Pageable pageable) {
        Page<Board> boardPage;

        // BoardCategory가 주어진 경우 해당 카테고리로 필터링
        if (boardCategory != null) {

            boardPage = boardRepository.findByBoardIdLessThanAndBoardCategoryOrderByBoardIdDesc(lastId, boardCategory, pageable);
        } else {
            // 카테고리 없이, lastId 보다 작은 게시글 반환
            boardPage = boardRepository.findByBoardIdLessThanOrderByBoardIdDesc(lastId, pageable);
        }

        // BoardResponse 객체로 변환하여 반환
        List<BoardListDTO> boardResponses = boardPage.getContent().stream()
                .map(BoardListDTO::new)  // BoardResponse 객체로 변환
                .collect(Collectors.toList());

        // BoardListResponse에 페이지 데이터 포함하여 반환
        return new BoardListResponse(boardResponses, boardPage.getTotalElements(), boardPage.getTotalPages());
    }
}
