package com.team3.code_nova.backend.entity;

import com.team3.code_nova.backend.enums.Status;
import com.team3.code_nova.backend.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder @Getter @Setter
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(unique = true, nullable = false, length = 255)
    private String username;

    @Column(length = 255)
    private String password;

    @Column(length = 255)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column
    private Status status;
}
