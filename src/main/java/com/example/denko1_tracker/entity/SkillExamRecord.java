package com.example.denko1_tracker.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "skill_exam_records")
@Data
public class SkillExamRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Integer problemNumber;

    @Column(nullable = false)
    private Integer attemptNumber;

    @Column(nullable = false)
    private Integer completionTimeMinutes;

    @Column(nullable = false)
    private Boolean isPassed;
}
