package com.smartfactory.predictive.backend.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "fab")
@Getter @Setter
@NoArgsConstructor
public class Fab {

    @Id
    @Column(name = "fab_id", length = 10)
    private String fabId;

    @Column(name = "fab_name", nullable = false, length = 50)
    private String fabName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}