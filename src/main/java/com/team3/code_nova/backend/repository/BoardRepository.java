package com.team3.code_nova.backend.repository;

import com.team3.code_nova.backend.entity.Board;
import com.team3.code_nova.backend.enums.BoardCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    
    //아이디로 보드 조회
    Board findBoardByBoardId(Long boardId);

    // 카테고리별 게시글 개수를 구하는 메서드
    long countByBoardCategory(BoardCategory boardCategory);

    // 전체 게시글 개수를 구하는 메서드
    long count();

    // lastId보다 작은 board_id 값을 가진 Board 객체들을 내림차순으로 정렬
    Page<Board> findByBoardIdLessThanOrderByBoardIdDesc(Long lastId, Pageable pageable);

    // lastId보다 작은 board_id 값을 가진 특정 카테고리의 Board 객체들을 내림차순으로 정렬
    Page<Board> findByBoardIdLessThanAndBoardCategoryOrderByBoardIdDesc(Long lastId, BoardCategory boardCategory, Pageable pageable);

    // 모든 카테고리에 대해 Board 객체들을 views에 따라 내림차순 정렬 반환
    Page<Board> findAllByOrderByViewsDesc(Pageable pageable);

    // 특정 카테고리의 Board 객체들을 views에 따라 내림차순 정렬 반환
    Page<Board> findByBoardCategoryOrderByViewsDesc(BoardCategory boardCategory, Pageable pageable);
}
