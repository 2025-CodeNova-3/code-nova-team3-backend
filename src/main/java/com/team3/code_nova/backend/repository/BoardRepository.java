package com.team3.code_nova.backend.repository;

import com.team3.code_nova.backend.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    // category가 "all"이 아닌 경우에만 category 필터를 적용
    Page<Board> findByCategoryAndIdLessThan(String category, Long lastId, Pageable pageable);

    // category가 "all"일 경우에는 전체 게시글을 가져옴
    Page<Board> findByIdLessThan(Long lastId, Pageable pageable);
}
