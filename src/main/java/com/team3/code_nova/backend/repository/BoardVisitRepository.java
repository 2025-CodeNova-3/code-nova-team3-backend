package com.team3.code_nova.backend.repository;

import com.team3.code_nova.backend.entity.BoardVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardVisitRepository extends JpaRepository<BoardVisit, Long> {
    // 방법 1: user.id와 board.id를 사용하여 쿼리 메서드 정의
    BoardVisit findByUser_UserIdAndBoard_BoardId(Long userId, Long boardId);
}
