package com.example.denko1_tracker.repository;

import com.example.denko1_tracker.entity.SkillExamRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SkillExamRecordRepository extends JpaRepository<SkillExamRecord, Long> {
    List<SkillExamRecord> findByUserIdOrderByProblemNumberAscAttemptNumberAsc(Long userId);
}
