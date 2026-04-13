package com.example.denko1_tracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "written_exam_records")
@Data
public class WrittenExamRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer examYear;

    @Column(nullable = false)
    private Integer attemptNumber;

    private Integer calcScore;    // 計算問題
    private Integer memoryScore;  // 記憶問題
    private Integer diagramScore; // 図面・回路
    private Integer lawScore;     // 法令

    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
