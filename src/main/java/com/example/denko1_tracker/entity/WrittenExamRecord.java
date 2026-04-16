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

    private String examPeriod; // 上期, 下期, 午前, 午後

    private Integer calcScore;    // 計算問題
    private Integer memoryScore;  // 記憶問題
    private Integer diagramScore; // 図面・回路
    private Integer lawScore;     // 法令

    private LocalDateTime updatedAt;

    public String getShortExamPeriod() {
        if (examPeriod == null) return "";
        return switch (examPeriod) {
            case "上期" -> "上";
            case "下期" -> "下";
            case "午前" -> "前";
            case "午後" -> "後";
            default -> "";
        };
    }

    public int getTotalScore() {
        return (calcScore != null ? calcScore : 0) +
               (memoryScore != null ? memoryScore : 0) +
               (diagramScore != null ? diagramScore : 0) +
               (lawScore != null ? lawScore : 0);
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
