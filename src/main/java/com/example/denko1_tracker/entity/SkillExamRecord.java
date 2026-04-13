package com.example.denko1_tracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "skill_exam_records")
@Data
public class SkillExamRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer problemNumber;

    @Column(nullable = false)
    private Integer attemptNumber;

    @Column(nullable = false)
    private Integer completionTimeMinutes;

    @Column(nullable = false)
    private Boolean isPassed;

    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
