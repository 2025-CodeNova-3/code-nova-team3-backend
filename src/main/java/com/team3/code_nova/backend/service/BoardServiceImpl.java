package com.team3.code_nova.backend.service;

import com.team3.code_nova.backend.dto.ApiResponse;
import com.team3.code_nova.backend.dto.auth.CustomUserDetails;
import com.team3.code_nova.backend.dto.BoardListDTO;
import com.team3.code_nova.backend.dto.request.BoardCreateRequest;
import com.team3.code_nova.backend.dto.response.BoardCategoryCountResponse;
import com.team3.code_nova.backend.dto.response.BoardCreateResponse;
import com.team3.code_nova.backend.dto.response.BoardVisitResponse;
import com.team3.code_nova.backend.dto.response.BoardListResponse;
import com.team3.code_nova.backend.entity.Board;
import com.team3.code_nova.backend.entity.BoardVisit;
import com.team3.code_nova.backend.entity.User;
import com.team3.code_nova.backend.enums.BoardCategory;
import com.team3.code_nova.backend.repository.BoardRepository;
import com.team3.code_nova.backend.repository.BoardVisitRepository;
import com.team3.code_nova.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final BoardVisitRepository boardVisitRepository;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, UserRepository userRepository, BoardVisitRepository boardVisitRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.boardVisitRepository = boardVisitRepository;
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
    public ResponseEntity<?> getBoardById(Long boardId) {
        try {
            // 현재 로그인한 사용자 정보 가져오기
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            Long userId = userDetails.getUserId();

            // 게시글 조회
            Board board = boardRepository.findBoardByBoardId(boardId);
            if (board == null) {
                return ResponseEntity.status(404).body(
                        new ApiResponse<>(404, -1, "게시글을 찾을 수 없습니다.", null)
                );
            }

            // 조회수 증가
            board.setViews(board.getViews() + 1);
            boardRepository.save(board);

            // BoardVisit 조회
            BoardVisit boardVisit = boardVisitRepository.findByUser_UserIdAndBoard_BoardId(userId, boardId);
            LocalDateTime openTime;
            String formattedOpenTime;

            // 포맷터 생성
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            if (boardVisit == null) {
                // 새로운 BoardVisit 생성
                BoardVisit newBoardVisit = new BoardVisit();
                newBoardVisit.setUser(userRepository.findById(userId).orElseThrow());
                newBoardVisit.setBoard(board);

                // openTime 설정 (현재시간 + openDuration)
                openTime = LocalDateTime.now().plusMinutes(board.getOpenDuration());
                formattedOpenTime = openTime.format(formatter); // 포맷팅된 시간
                newBoardVisit.setOpenTime(openTime);

                boardVisitRepository.save(newBoardVisit);
            } else {
                openTime = boardVisit.getOpenTime();
                formattedOpenTime = openTime.format(formatter); // 포맷팅된 시간
            }

            // Response DTO 생성
            BoardVisitResponse response = BoardVisitResponse.builder()
                    .boardId(board.getBoardId())
                    .title(board.getTitle())
                    .boardCategory(board.getBoardCategory().name())
                    .openContent(board.getOpenContent())
                    .hiddenContent(board.getHiddenContent())
                    .views(board.getViews())
                    .openTime(formattedOpenTime) // 포맷팅된 시간을 Response에 전달
                    .authorName(board.getUser().getUsername())
                    .createdAt(board.getCreatedAt())
                    .build();

            return ResponseEntity.ok(
                    new ApiResponse<>(200, 0, "게시글 조회 성공", response)
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(500, -1, "서버 오류가 발생했습니다: " + e.getMessage(), null)
            );
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
