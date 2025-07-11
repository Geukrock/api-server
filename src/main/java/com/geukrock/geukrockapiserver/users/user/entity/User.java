package com.geukrock.geukrockapiserver.users.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter
@Builder
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private String id;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(length = 11)
    private String phoneNumber;

    @Column
    private LocalDateTime createdAt;
}
