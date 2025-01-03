package com.team3.code_nova.backend.repository;

import com.team3.code_nova.backend.dto.response.BoardRecentVisitResponse;
import com.team3.code_nova.backend.entity.Board;
import com.team3.code_nova.backend.entity.BoardVisit;
import com.team3.code_nova.backend.enums.BoardCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    
    //아이디로 보드 조회
    Board findBoardByBoardId(Long boardId);

    // 카테고리별 게시글 개수를 구하는 메서드
    long countByBoardCategory(BoardCategory boardCategory);

    // 전체 게시글 개수를 구하는 메서드
    long count();

    // 전체 게시글을 가져오는 메서드
    Page<Board> findAll(Pageable pageable);

    // 카테고리로 필터링된 게시글을 가져오는 메서드
    Page<Board> findByBoardCategoryOrderByBoardIdDesc(BoardCategory boardCategory, Pageable pageable);

    // 모든 카테고리에 대해 Board 객체들을 views에 따라 내림차순 정렬 반환
    Page<Board> findAllByOrderByViewsDesc(Pageable pageable);

    // 특정 카테고리의 Board 객체들을 views에 따라 내림차순 정렬 반환
    Page<Board> findByBoardCategoryOrderByViewsDesc(BoardCategory boardCategory, Pageable pageable);


    @Query("SELECT new com.team3.code_nova.backend.dto.response.BoardRecentVisitResponse(b, bv.openTime) " +
            "FROM Board b " +
            "INNER JOIN BoardVisit bv ON b.boardId = bv.board.boardId " +
            "WHERE bv.user.userId = :userId " +
            "ORDER BY bv.recentTime DESC")
    List<BoardRecentVisitResponse> findRecentBoardsWithOpenTimeByUserId(@Param("userId") Long userId, Pageable pageable);

    // 제목에 특정 키워드를 포함한 Board 객체의 개수를 반환
    long countByTitleContaining(String titleKeyword);

    Page<Board> findByTitleContainingOrderByBoardIdDesc(String titleKeyword, Pageable pageable);
}
