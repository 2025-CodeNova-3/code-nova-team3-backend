package com.team3.code_nova.backend.entity;

import com.team3.code_nova.backend.enums.BoardCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "boards")
public class Board extends BaseEntity {
    //필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    private BoardCategory boardCategory;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = User.class)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String openContent;

    @Column(columnDefinition = "TEXT")
    private String hiddenContent;

    // 해당 게시글의 hidden content 공개 허용 대기 시간 (단위 : 분)
    private Integer openDuration;

    private Long views;
}