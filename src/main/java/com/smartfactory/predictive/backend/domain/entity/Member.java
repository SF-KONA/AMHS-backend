package com.smartfactory.predictive.backend.domain.entity;

import com.smartfactory.predictive.backend.domain.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter @Setter
@NoArgsConstructor
public class Member {

    @Id
    @Column(name = "login_id", length = 20)
    private String loginId;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(name = "member_name", nullable = false, length = 50)
    private String memberName;

    @Column(nullable = false, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}