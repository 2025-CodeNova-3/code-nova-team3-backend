package com.team3.code_nova.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = User.class)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Board.class)
    @JoinColumn(name = "board_id", updatable = false)
    private Board board;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean beforeOpen;
}
