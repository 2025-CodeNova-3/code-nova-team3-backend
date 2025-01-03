package com.team3.code_nova.backend.repository;

import com.team3.code_nova.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    // 사용자 이름으로 조회
    Optional<User> findByUsername(String username);
}
